import { Component, EventEmitter, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from "@angular/forms";
import { MatSnackBar } from '@angular/material/snack-bar';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Volunteer } from 'src/app/user/models/volunteer.model';
import { VolunteerService } from 'src/app/user/services/volunteer/volunteer.service';

@Component({
  selector: 'app-volunteer-form',
  templateUrl: './volunteer-form.component.html',
  styleUrls: ['./volunteer-form.component.css']
})
export class VolunteerFormComponent {
  @Output() submitForm = new EventEmitter<Volunteer>()
  registrationForm: FormGroup;
  domains: Domain[] = [];
  genders: String[] = ["FEMALE", "MALE"];
  
  constructor(private formBuilder: FormBuilder, private volunteerService: VolunteerService,
     private _snackBar: MatSnackBar, private domainService: DomainService) {
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
    const nameRegex = /^[A-Z][a-z]{2,}$/
    return this.formBuilder.group({
      // check if unique
      user: new FormControl('', [Validators.required, Validators.minLength(5)]),
      surname: new FormControl('', [Validators.required, Validators.minLength(3), Validators.pattern(nameRegex)]),
      firstname: new FormControl('', [Validators.required, Validators.minLength(3), Validators.pattern(nameRegex)]),
      email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]),
      password: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
      confirmationPassword: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
      phoneNr: new FormControl('', [Validators.required, Validators.pattern('^[0][7][0-9]{8}$')]),
      age: new FormControl('', [Validators.required, Validators.min(14), Validators.pattern('[1-9][0-9]')]),
      description: new FormControl(''),
      gender: new FormControl(''),
      domains:  new FormControl(''),
      volunteered:  new FormControl('')
    })
  }

  onSubmit(formDirective: FormGroupDirective) {
    console.log(this.registrationForm.value);
    this.volunteerService.registration(this.registrationForm.value).subscribe({
      next: () => {
        this._snackBar.open('Successfull registration! Your account is created.', 'OK', {
          duration: 10000,
          panelClass: 'success-snackbar'
        });
      },
      // setTimeout(() => this.router.navigate(["/login"]), 1000);
      error: (e: { error: { message: string; }; }) => {
        this._snackBar.open(e.error.message, 'OK', {
        duration: 10000,
        panelClass: 'fail-snackbar'
      })
      }
    })
    this.registrationForm.reset();
    formDirective.resetForm();
  }

  get passwordFormControl(): AbstractControl {
    return this.registrationForm.get('password') as AbstractControl;
  }
 
  get confirmPasswordFormControl(): AbstractControl {
    return this.registrationForm.get('confirmationPassword') as AbstractControl;
  }

}
