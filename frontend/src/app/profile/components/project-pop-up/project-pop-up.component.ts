import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Project } from 'src/app/project/models/project.model';
import { ProjectService } from 'src/app/project/services/project.service';
@Component({
  selector: 'app-project-pop-up',
  templateUrl: './project-pop-up.component.html',
  styleUrls: ['./project-pop-up.component.css']
})
export class ProjectPopUpComponent implements OnInit{
  givenData: Project
  // 1 week from now
  minDate = new Date(Date.now() + (3600 * 1000 * 168)); 
  // 3 months from now
  maxDate = new Date(Date.now() + (3600 * 1000 * 2160)); 
  domains: Domain[] = [];
  projectForm!: FormGroup;
  selectedDomains: Domain[] = [];
  oldName: String = '';

  constructor(private fb: FormBuilder, private dialog: MatDialogRef<ProjectPopUpComponent>, private domainService: DomainService,
    @Inject(MAT_DIALOG_DATA) data: Project, private _snackBar: MatSnackBar, private projectService: ProjectService){
      this.givenData = data
      this.oldName = data.project_name
    }

  ngOnInit(): void {
    this.projectForm = this.fb.group({
      project_name: [this.givenData.project_name],
      project_description: [this.givenData.project_description],
      img: [this.givenData.project_img],
      expiration_date:  [this.givenData.expiration_date],
      domains:  [this.givenData.domains]
    })
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
  )
    this.selectedDomains = this.givenData.domains
  }

  close() {
    this.dialog.close();
  }

  uploadFile(event: any) {
    const file = (event.target as HTMLInputElement).files![0];
    this.projectForm.patchValue({
      file: file,
    });
    this.projectForm.get('img')!.updateValueAndValidity();
  }

  save(formDirective: FormGroupDirective) {
      let formData: any = new FormData();
      let project: Partial<Project> = this.projectForm.value as Partial<Project>;
      project.project_img = undefined;
      formData.append('project', JSON.stringify(project));
      formData.append('file', this.projectForm.controls['img'].value);
      const date = this.projectForm.controls['expiration_date'].value
      project.expiration_date = new Date(+date[0], +date[1], +date[2]);
      this.projectService.update(this.oldName, formData).subscribe({
        next: () => {
          this._snackBar.open('Successfully saved project!', 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
        },
        error: (e: { error: { message: string; }; }) => 
          this._snackBar.open(e.error.message, 'OK', {
          duration: 10000,
          panelClass: 'fail-snackbar'
        })
        });
      this.dialog.close();
      window.location.reload();
    }
}
