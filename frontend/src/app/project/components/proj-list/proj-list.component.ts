import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import jwt_decode from 'jwt-decode';
import { formatDate } from '@angular/common';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Organization } from 'src/app/user/models/organization.model';
import { OrgService } from 'src/app/user/services/org/org.service';
import { UserService } from 'src/app/user/services/user/user.service';

@Component({
  selector: 'app-proj-list',
  templateUrl: './proj-list.component.html',
  styleUrls: ['./proj-list.component.css']
})
export class ProjListComponent implements OnInit {
  projects: Project[] = [];
  logedin_user_role: String = '';
  logedin_user_projects: String[] = [];
  favourite_projects: String[] = [];
  current_date: String = '';
  domains: Domain[] = []
  orgs: Organization[] = []
  roles: String[] = ["organization", "user"]
  selected_sort: String = "Newest"

  params_domain: String = '';
  query_params_domain: String[] = []
  query_params_owner: String | null | undefined
  query_params_org: String[] = []
  past_projects: boolean = false

  constructor(private projectService: ProjectService, private userService: UserService, private domainService: DomainService,
    private orgService: OrgService, private router: Router, private _snackBar: MatSnackBar, private activatedRoute: ActivatedRoute){

      this.activatedRoute.params.subscribe((param) => {
        this.params_domain = param['domain']
        console.log( param['domain'])

        // filter by categories init
        this.domainService.getAllDomains().subscribe((result: Domain[]) => {
          this.domains = result
          if( param['domain'] != undefined) {
            if(result.find((e: Domain) => e.domain_name === param['domain'] )) {
                this.projectService.getProjectsByDomain(param['domain']).subscribe(result => {
                        this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
                      })
              }
              // not domain param but searched value!
              else {
                this.projectService.getProjectsBySearch(param['domain']).subscribe(result => {
                        this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
                      })
              }
            }
          }
        );
      })

      this.activatedRoute.queryParams.subscribe((param) => {
        this.query_params_domain = param['domains']
        this.query_params_owner = param['owner']
        this.query_params_org = param['org']
        let domain_param =  this.activatedRoute.snapshot.params['domain']
        
        if( param['domains'] != undefined ||  param['owner'] != undefined ||  param['org'] != undefined) {
          this.projectService.getProjectsFiltered(['domain', 'owner', 'orgs'], param['domains'], 
          param['owner'], param['org'], domain_param).subscribe(result => {
              this.projects = result.filter(p => p.expiration_date.toString() > this.current_date)
            });
            
        }
      })
    }

  // if not expired
  getAllProjects(): void {
    let domain_param = this.activatedRoute.snapshot.params['domain']
    if (domain_param == undefined) {
      this.projectService.getAll().subscribe(result => {
        this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
      });
    }
    else {
      this.projectService.getProjectsByDomain(domain_param).subscribe(result => {
        this.projects = result.filter(p => p.expiration_date.toString() > this.current_date);
      })
    }
  }

  getAllOrgs(): void {
    this.orgService.getAll().subscribe(result => {
      this.orgs = result
    });
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        const name = token.sub;
        this.userService.getRole(name).subscribe(result => {
          this.logedin_user_role = result.role;
          if (result.role == 'USER') {
            this.projectService.getFavouriteProjects().subscribe(result => {
              this.favourite_projects = result.filter(p => p.expiration_date.toString() > this.current_date).map(item => item.project_name);
              console.log("users fav:", result.filter(p => p.expiration_date.toString() > this.current_date).map(item => item.project_name));}
              )
          }
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
    // console.log(this.logedin_user_role == 'USER')
      
    
  }
   
  ngOnInit(): void {
    if (window.location.pathname == "/projects") {
      this.getAllProjects()
    }
    this.getAllOrgs();
    // set owned projects + role of user 
    this.getRole();
    this.getUsersProjects();
    
    this.current_date = formatDate(new Date(), 'yyyy-MM-dd', 'en_US');

  }

  clickDelete(username: String): void {
    this.projectService.delete(username).subscribe({
      next: (result: any) => {
          this._snackBar.open("You deleted the "+username +" project!", 'OK', {
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

  clickDeactivate(username: String): void {
    this.projectService.deactivate(username).subscribe({
      next: (result: any) => {
          this._snackBar.open("You deactivate the "+username +" project!", 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
          this.router.navigate(["/projects"]);
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
  changeProjects(past_projects: boolean): void {
    this.past_projects = past_projects
    if (past_projects == true) {
      this.projectService.getAll().subscribe(result => {
        this.projects = result.filter(p => p.expiration_date.toString() <= this.current_date);
      });
    }
    else {
      this.getAllProjects();
    }
  }

  prepateDomainQuery(): String|null {
    let domain_param =  this.activatedRoute.snapshot.params['domain']
    if (domain_param != null) {
      domain_param = '/' + domain_param
    }
    return domain_param
  }

  filterByDomain(values:any):void {
    let domain_param = this.prepateDomainQuery()
    this.query_params_owner = this.activatedRoute.snapshot.queryParams['owner']
    if (domain_param == undefined) {
      domain_param = ''
    }
    if(values.checked) {
      // already some domain filter params set
      if (!!this.query_params_domain) {
        let params = [this.query_params_domain, values.source.value]
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': params, 
        "owner" : this.query_params_owner, "org": this.query_params_org } });
      }
      // setting 1. domain filter
      else {
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': values.source.value, 
        "owner" : this.query_params_owner, "org": this.query_params_org} })
      }
    }
    else {  //unchecked
      let params = this.activatedRoute.snapshot.queryParams['domains']
      // if only 1 domain is set -> remove domain filter
      if (this.query_params_domain == values.source.value ){
        if (!!this.query_params_owner || !!this.query_params_org) {
          this.router.navigate(['/projects'+domain_param], { queryParams: { "owner" : this.query_params_owner, "org": this.query_params_org} });
        }
        else {
          this.getAllProjects()
          this.router.navigate(['/projects'+domain_param])
        }
      } //if more domains are selected and remove just the unchecked one
      else {
        params = params.map((item: string) => item.split(","))
        params = Array.prototype.concat.apply([], params);
        params  = params.filter((obj: String) => obj !== values.source.value)
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': params ,  "owner" : this.query_params_owner} });
      }
    }
  }
  
  filterByOwner(values:any):void {
    let domain_param = this.prepateDomainQuery()
    if (domain_param == undefined) {
      domain_param = ''
    }
    if(values.checked) {
      // if both owner types selected
      if(!!this.query_params_owner) {
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain, "org": this.query_params_org } });
      }
      // new owner type selected(only 1)
      else {
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain,
         "owner" : values.source.value, "org": this.query_params_org }})
      }
    }
    else {
      // uncheck the only set owner type -> none remains
      if ( this.query_params_owner == values.source.value)
        if (!!this.query_params_domain || !!this.query_params_org) {
            this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain, "org": this.query_params_org } });
        }
        else {
          this.getAllProjects()
          this.router.navigate(['/projects'+domain_param])
        }
    }
  }

  filterByOrgs(values:any):void {
    let domain_param = this.prepateDomainQuery()
    this.query_params_owner = this.activatedRoute.snapshot.queryParams['owner']
    if (domain_param == undefined) {
      domain_param = ''
    }
    if(values.checked) {
      if(!!this.query_params_org) {
        let params = [this.query_params_org, values.source.value]
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain, 
        "owner" : this.query_params_owner, "org": params } });
      }
      else {
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain, 
        "owner" : this.query_params_owner, "org": values.source.value }})
      }
    }
    else {
      let params = this.activatedRoute.snapshot.queryParams['org']
      if (params == values.source.value ){
        if (!!this.query_params_owner || !!this.query_params_domain) {
          this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain, "owner" : this.query_params_owner} });
        }
        else {
          this.getAllProjects()
          this.router.navigate(['/projects'+domain_param])
        }
      }
      else {
        params = params.map((item: string) => item.split(","))
        params = Array.prototype.concat.apply([], params);
        params  = params.filter((obj: String) => obj !== values.source.value)
        this.router.navigate(['/projects'+domain_param], { queryParams: { 'domains': this.query_params_domain,
        "owner" : this.query_params_owner, "org": params} });
      }
    }
  }

  sortProduct(type: String, by:String): void{
    if  (by == "name") {
      if (type == "+") {
        this.selected_sort = "Ascending by Name"
        this.projects.sort((a, b) => a.project_name.localeCompare(b.project_name));
      }
      else {
        this.selected_sort = "Descending by Name"
        this.projects.sort((a, b) => b.project_name.localeCompare(a.project_name));
      }
    }
    else if  (by == "expiration") {
      if (type == "+") {
        this.selected_sort = "Ascending by expiration date"
        this.projects.sort((a, b) => a.expiration_date.toString().localeCompare(b.expiration_date.toString()));
      }
      else {
        this.selected_sort = "Descending by expiration date"
        this.projects.sort((a, b) => b.expiration_date.toString().localeCompare(a.expiration_date.toString()));
      }
    }
    else if  (by == "Newest") {
      this.selected_sort = by
      this.projects.sort((a, b) => a.creation_date.toString().localeCompare(b.creation_date.toString()));
    }
    else if  (by == "Most popular") {
      this.selected_sort = by
      this.projectService.getFavouriteProjectsSorted().subscribe(result => {
        let names = this.projects.map(item => item.project_name);
        this.projects = result.filter(p => p.expiration_date.toString() > this.current_date && names.indexOf(p.project_name) !== -1);  
      } )
    }
  }

  selectFavourite(project_name: String): void {
    // already selected
    console.log(this.favourite_projects, project_name, this.favourite_projects.indexOf(project_name))
    if (this.favourite_projects.indexOf(project_name) > -1) {
      this.projectService.removeFavouriteProjects(project_name).subscribe(result => {
        this.favourite_projects = result.map(item => item.project_name);
      });
    }
    else {
      this.projectService.addFavouriteProject(project_name).subscribe(result => {
        this.favourite_projects = result.map(item => item.project_name);
        });
    }
  }
}