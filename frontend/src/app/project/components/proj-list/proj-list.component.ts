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
  filter_by_domain: String = '';
  domains: Domain[] = []

  checked = false;
  indeterminate = false;
  labelPosition: 'before' | 'after' = 'after';
  disabled = false;

  constructor(private projectService: ProjectService, private userService: LoginService, private domainService: DomainService,
    private router: Router, private _snackBar: MatSnackBar, private activatedRoute: ActivatedRoute){}

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
    // if 1 domain in request params
    this.activatedRoute.params.subscribe((param) => {
      this.filter_by_domain =  param['domain'];
      if ( !!this.filter_by_domain ) { 
        this.projectService.getProjectsByDomain(param['domain']).subscribe(result => {
        
          this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
          console.log(this.filter_by_domain)
        })     
      } 
      else {
        this.getAllProjects();
      }
      })
    
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
      console.log(values.source.value)
      
    }
  }


}

