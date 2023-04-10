import { Component, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, FormGroupDirective, AbstractControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Domain } from 'src/app/domain/models/domain.model';
import { Organization } from 'src/app/feature/models/organization.model';
import { OrgService } from 'src/app/feature/services/user/org/org.service';
import { DomainService } from 'src/app/domain/services/domain.service';

@Component({
  selector: 'app-org-form',
  templateUrl: './org-form.component.html',
  styleUrls: ['./org-form.component.css']
})
export class OrgFormComponent {
  @Output() submitForm = new EventEmitter<Organization>()
  registrationForm: FormGroup;
  domains: Domain[] = [];
  selectedDomiains: Array<any[]> = [];
  
  constructor(private formBuilder: FormBuilder, private orgService: OrgService, private _snackBar: MatSnackBar, private domainService: DomainService) {
    this.registrationForm = this.initForm();
  }

  ngOnInit(): void {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    )
  }

  private initForm(): FormGroup {
    const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    const passwordRegex = /^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/
    // ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$
    return this.formBuilder.group({
      user: new FormControl('', [Validators.required, Validators.minLength(5)]),
      email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]),
      password: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
      confirmationPassword: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
      phoneNr: new FormControl('', [Validators.required, Validators.pattern('^[0][7][0-9]{8}$')]),
      address: new FormControl(''),
      description: new FormControl(''),
      website: new FormControl(''),
      logo:  new FormControl(''),
      domains:  new FormControl(''),
    })
  }

  onSubmit(formDirective: FormGroupDirective) {
    console.log(this.registrationForm.value)
    this.registrationForm.removeControl('confirmationPassword')
    let formData: any = new FormData();
    let org: Partial<Organization> = this.registrationForm.value as Partial<Organization>;
    org.logo = undefined;
    formData.append('organization', JSON.stringify(org));
    formData.append('file', this.registrationForm.controls['logo'].value);
    console.log( JSON.stringify(org), this.registrationForm.controls['logo'].value)
    this.orgService.registration(formData).subscribe({
      next: () => {
        this._snackBar.open('Successfull registration! Your account is created!', 'OK', {
          duration: 10000,
          panelClass: 'success-snackbar'
        });
      },
      error: (e: { error: { message: string; }; }) => {
          this._snackBar.open(e.error.message, 'OK', {
            duration: 10000,
            panelClass: 'fail-snackbar'
          })
          console.log(e);
        }
      });

    this.registrationForm.reset();
    formDirective.resetForm();
    this.selectedDomiains = []
    }

  get passwordFormControl(): AbstractControl {
    return this.registrationForm.get('password') as AbstractControl;
  }
 
  get confirmPasswordFormControl(): AbstractControl {
    return this.registrationForm.get('confirmationPassword') as AbstractControl;
  }

  uploadFile(event: any) {
    const file = (event.target as HTMLInputElement).files![0];
    this.registrationForm.patchValue({
      logo: file,
    });
    this.registrationForm.get('logo')!.updateValueAndValidity();
  }
}
