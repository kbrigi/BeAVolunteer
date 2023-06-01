import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from 'src/app/project/models/project.model';
import { ProjectService } from 'src/app/project/services/project.service';
import { ProjectPopUpComponent } from '../project-pop-up/project-pop-up.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

@Component({
  selector: 'app-projects-page',
  templateUrl: './projects-page.component.html',
  styleUrls: ['./projects-page.component.css']
})
export class ProjectsPageComponent implements OnInit{
  tab: String = ''
  projects: Project[] = []

  constructor(private projectService: ProjectService,private activatedRoute: ActivatedRoute,  
    private _snackBar: MatSnackBar, private router: Router, private dialog: MatDialog){
      this.activatedRoute.params.subscribe((param) => {
        this.tab = param['page']
        console.log(param['page'])
        if (localStorage.getItem('token') !== null) {
          if (param['page'] == 'Own projects') {
            this.projectService.getProjectsByOwner().subscribe(result => {
              this.projects = result;
              console.log(result)
            });
          }
          else if (param['page'] == 'Favourites') {
            this.projectService.getFavouriteProjects().subscribe(result => {
              this.projects = result;
              console.log(result)
            });
          }     
        }
      })
  }

  ngOnInit(): void {
    
  }

  openModifyPopUp(project: Project) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      project_name: project.project_name,
      project_description: project.project_description,
      img: project.project_img,
      expiration_date: project.expiration_date,
      domains: project.domains
  };
    this.dialog.open(ProjectPopUpComponent, dialogConfig);
  }

  clickDelete(username: String): void {
    this.projectService.delete(username).subscribe({
      next: (result: any) => {
          this._snackBar.open("You deleted the "+ username +" project!", 'OK', {
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



}
