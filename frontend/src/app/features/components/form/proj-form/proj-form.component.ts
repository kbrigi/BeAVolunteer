import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Project } from 'src/app/features/models/project.model';
import { ProjectService } from 'src/app/features/services/project/project.service';

@Component({
  selector: 'app-proj-form',
  templateUrl: './proj-form.component.html',
  styleUrls: ['./proj-form.component.css']
})
export class ProjFormComponent {
  @Output() submitForm = new EventEmitter<Project>();
  projForm: FormGroup;
  // 1 week from now
  minDate = new Date(Date.now() + (3600 * 1000 * 168)); 
  // 3 months from now
  maxDate = new Date(Date.now() + (3600 * 1000 * 2160)); 

  
  constructor(private formBuilder: FormBuilder, private projectService: ProjectService, private _snackBar: MatSnackBar) {
    this.projForm = this.initForm();
  }

  private initForm(): FormGroup {
    return this.formBuilder.group({
      project_name: new FormControl('', [Validators.required, Validators.minLength(5)]),
      project_description: new FormControl('', [Validators.required, Validators.minLength(15)]),
      img: new FormControl('', [Validators.required]),
      expiration_date:  new FormControl('', [Validators.required]),
    })
  }

  uploadFile(event: any) {
    const file = (event.target as HTMLInputElement).files![0];
    this.projForm.patchValue({
      file: file,
    });
    this.projForm.get('img')!.updateValueAndValidity();
  }

  onSubmit(formDirective: FormGroupDirective) {
    console.log(this.projForm.value)
    let formData: any = new FormData();
    let org: Partial<Project> = this.projForm.value as Partial<Project>;
    formData.append('project', JSON.stringify(org));
    formData.append('file', this.projForm.controls['img'].value);
    this.projectService.save(formData).subscribe(
      () => {
        this._snackBar.open('Successfully saved project!', 'OK', {
          duration: 10000,
          panelClass: 'success-snackbar'
        });
      (e: { error: { message: string; }; }) => console.log(e.error);
      });

    this.projForm.reset();
    formDirective.resetForm();
    }

}
