import { Component, EventEmitter, Output } from '@angular/core';
import { Domain } from '../../models/domain.model';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { DomainService } from '../../services/domain.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-domain-form',
  templateUrl: './domain-form.component.html',
  styleUrls: ['./domain-form.component.css']
})
export class DomainFormComponent {
  @Output() submitForm = new EventEmitter<Domain>();
  domainForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private domainService: DomainService, private _snackBar: MatSnackBar) {
    this.domainForm = this.initForm();
  }

  private initForm(): FormGroup {
    return this.formBuilder.group({
      domain_name: new FormControl('', [Validators.required, Validators.minLength(5)]),
      domain_img: new FormControl('', [Validators.required]),
    })
  }

  uploadFile(event: any) {
    const file = (event.target as HTMLInputElement).files![0];
    this.domainForm.patchValue({
      file: file,
    });
    this.domainForm.get('domain_img')!.updateValueAndValidity();
  }

  onSubmit(formDirective: FormGroupDirective) {
    let formData: any = new FormData();
    let domain: Partial<Domain> = this.domainForm.value as Partial<Domain>;
    domain.domain_img = undefined;
    formData.append('domain', JSON.stringify(domain));
    formData.append('file', this.domainForm.controls['domain_img'].value);
    this.domainService.save(formData).subscribe({
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

    this.domainForm.reset();
    formDirective.resetForm();
    }
}
