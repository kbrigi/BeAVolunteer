import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/feature/services/user/login/login.service';
import jwt_decode from 'jwt-decode';
import { formatDate } from '@angular/common';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Organization } from 'src/app/feature/models/organization.model';

@Component({
  selector: 'app-proj-list',
  templateUrl: './proj-list.component.html',
  styleUrls: ['./proj-list.component.css']
})
export class ProjListComponent implements OnInit {
  projects: Project[] = [];
  logedin_user_role: String = '';
  logedin_user_projects: String[] = [];
  current_date: String = '';
  domains: Domain[] = []
  orgs: Organization[] = []
  roles: String[] = ["organization", "user"]

  params_domain: String = '';
  query_params_domain: String[] = []
  query_params_owner: String = ''
  query_params_org: String[] = []

  constructor(private projectService: ProjectService, private userService: LoginService, private domainService: DomainService,
    private router: Router, private _snackBar: MatSnackBar, private activatedRoute: ActivatedRoute){
      this.activatedRoute.params.subscribe((param) => {
        this.params_domain = param['domain']
        if( param['domain'] != undefined) {
          this.projectService.getProjectsByDomain(this.params_domain).subscribe(result => {
                  this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
                })
        }
      })

      this.activatedRoute.queryParams.subscribe((param) => {
        this.query_params_domain = param['domains']
        this.query_params_owner = param['owner']
        if(!!param['domains'] && !! param['owner']) {
          this.projectService.getProjectsFiltered(['domain', 'owner'],this.query_params_domain, this.query_params_owner).subscribe(result => {
            this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
          })
        }
        else if(!!param['domains']) {
          this.projectService.getProjectsFiltered(['domain'],this.query_params_domain, undefined).subscribe(result => {
                  this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
                })
        }
        else if(!!param['owner']) {
          this.projectService.getProjectsFiltered(['owner'], [this.query_params_owner], undefined).subscribe(result => {
                  this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
                })
        }
      })
    }

  // if not expired
  getAllProjects(): void {
    this.projectService.getAll().subscribe(result => {
      this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
    });
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        const name = token.sub;
        this.userService.getRole(name).subscribe(result => {
          this.logedin_user_role = result.role;
        }
        );
      }
  }

  getUsersProjects(): void {
    if (localStorage.getItem('token') !== null) {
      this.projectService.getProjectsByOwner().subscribe(result => {
        this.logedin_user_projects = result.filter(p => p.expiration_date.toString() > this.current_date).map(item => item.project_name);
        console.log("users projects:", result);
      });
    }
  }
   
  ngOnInit(): void {
    this.getAllProjects();

    // set owned projects + role of user 
    this.getRole();
    this.getUsersProjects();
    
    this.current_date = formatDate(new Date(), 'yyyy-MM-dd', 'en_US');

    // filter by categories init
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    );

  }

  clickDelete(username: String): void {
    this.projectService.delete(username).subscribe({
      next: (result: any) => {
          this._snackBar.open("You deactivate the "+username +" oranization!", 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
          this.router.navigate(["/orgs"]);
      },
      error: (e: { error: { message: string; }; }) => { 
      this._snackBar.open(e.error.message, 'OK', {
        duration: 10000,
        panelClass: 'fail-snackbar'
      })
      console.log(e);
    }
    });

  }

  // past/current project change
  changeProjects(past_projects: Boolean): void {
    if (past_projects == true) {
      this.projectService.getAll().subscribe(result => {
        this.projects = result.filter(p => p.expiration_date.toString() <= this.current_date);
      });
    }
    else {
      this.getAllProjects();
    }
  }

  filterByDomain(values:any):void {
    if(values.checked) {
      if (!!this.query_params_domain) {
        let params = [this.query_params_domain, values.source.value]
        this.router.navigate(['/projects'], { queryParams: { 'domains': params, "owner" : this.query_params_owner } });
      }
      else {
        this.router.navigate(['/projects'], { queryParams: { 'domains': values.source.value, "owner" : this.query_params_owner} })
      }
    }
    else {
      let params = this.activatedRoute.snapshot.queryParams['domains']
      if (this.query_params_domain == values.source.value ){
        if (!!this.query_params_owner) {
          this.router.navigate(['/projects'], { queryParams: { "owner" : this.query_params_owner} });
        }
        else {
          this.getAllProjects()
          this.router.navigate(['/projects'])
        }
      }
      else {
        params = params.map((item: string) => item.split(","))
        params = Array.prototype.concat.apply([], params);
        params  = params.filter((obj: String) => obj !== values.source.value)
        this.router.navigate([], { queryParams: { 'domains': params ,  "owner" : this.query_params_owner} });
      }
    }
  }
  
  filterByOwner(values:any):void{
    if(values.checked) {
      if(!!this.query_params_owner) {
        this.getAllProjects()
        this.router.navigate(['/projects'], { queryParams: { 'domains': this.query_params_domain } });
      }
      else {
        this.router.navigate(['/projects'], { queryParams: { 'domains': this.query_params_domain, "owner" : values.source.value }})
      }
    }
    else {
      if ( this.query_params_owner == values.source.value)
        if (!!this.query_params_domain) {
            this.router.navigate(['/projects'], { queryParams: { 'domains': this.query_params_domain } });
        }
        else {
          this.getAllProjects()
          this.router.navigate(['/projects'])
        }
    }
  }
}

