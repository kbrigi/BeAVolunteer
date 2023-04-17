import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Project } from 'src/app/feature/models/project.model';
import { ProjectService } from 'src/app/feature/services/project/project.service';

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
  domains: Domain[] = [];
  
  constructor(private formBuilder: FormBuilder, private projectService: ProjectService, private _snackBar: MatSnackBar, private domainService: DomainService) {
    this.projForm = this.initForm();
  }

  ngOnInit(): void {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    )
  }

  private initForm(): FormGroup {
    return this.formBuilder.group({
      project_name: new FormControl('', [Validators.required, Validators.minLength(5)]),
      project_description: new FormControl('', [Validators.required, Validators.minLength(15)]),
      img: new FormControl('', [Validators.required]),
      expiration_date:  new FormControl('', [Validators.required]),
      domains:  new FormControl('')
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
    let formData: any = new FormData();
    let proj: Partial<Project> = this.projForm.value as Partial<Project>;
    formData.append('project', JSON.stringify(proj));
    formData.append('file', this.projForm.controls['img'].value);
    this.projectService.save(formData).subscribe({
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

    this.projForm.reset();
    formDirective.resetForm();
    }

}
