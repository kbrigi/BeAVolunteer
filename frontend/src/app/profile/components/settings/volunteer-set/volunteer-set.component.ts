import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import jwt_decode from 'jwt-decode';
import { Domain } from 'src/app/domain/models/domain.model';
import { DomainService } from 'src/app/domain/services/domain.service';
import { Organization } from 'src/app/user/models/organization.model';
import { Volunteer } from 'src/app/user/models/volunteer.model';
import { OrgService } from 'src/app/user/services/org/org.service';
import { UserService } from 'src/app/user/services/user/user.service';
import { VolunteerService } from 'src/app/user/services/volunteer/volunteer.service';

@Component({
  selector: 'app-volunteer-set',
  templateUrl: './volunteer-set.component.html',
  styleUrls: ['./volunteer-set.component.css']
})
export class VolunteerSetComponent implements OnInit{
  logedin_user_role: String = '';
  givenData!: Volunteer | Organization
  domains: Domain[] = [];
  projectForm!: FormGroup;
  oldName: String = '';
  form!: FormGroup;

  constructor(private fb: FormBuilder, private activatedRoute: ActivatedRoute, private _snackBar: MatSnackBar, 
     private volunteerService: VolunteerService, private domainService: DomainService, private userService: UserService,
     private orgService: OrgService){    
      this.form = this.initForm()
      this.activatedRoute.params.subscribe((param) => {
      this.oldName = param['name']
      // set role
      if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        let username = token.sub;
        this.userService.getRole(username).subscribe(result => {
          this.logedin_user_role = result.role;

          if (result.role == 'USER') {
            this.volunteerService.getByName(param['name']).subscribe(result => {
              this.givenData = result;
              this.form.patchValue({
                user: result.user, 
                surname: result.surname, 
                firstname: result.firstname, 
                email: result.email,  
                password: result.password,
                phoneNr: result.phoneNr,
                age: result.age,
                domains: result.domains,
                volunteered: result.volunteered,
                description: result.description
              });
            }) 
          }
          else if (result.role == 'ORGANIZATION'){
            this.orgService.getOrg(param['name']).subscribe(result => {
              this.givenData = result;
              this.form.patchValue({
                user: result.user, 
                email: result.email,  
                phoneNr: result.phoneNr,
                domains: result.domains,
                description: result.description,
                address: result.address,
                website: result.website
              });
            }) 
          } 
        } );
      }
    })
  }

  private initForm(): FormGroup {
    const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    const passwordRegex = /^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/
    const nameRegex = /^[A-Z][a-z]{2,}$/
    return this.fb.group({
      user: new FormControl('', [Validators.minLength(5)]),
      surname: new FormControl('', [Validators.minLength(3), Validators.pattern(nameRegex)]),
      firstname: new FormControl('', [Validators.minLength(3), Validators.pattern(nameRegex)]),
      email: new FormControl('', [Validators.pattern(emailRegex)]),
      password: new FormControl('', [Validators.pattern(passwordRegex)]),
      phoneNr: new FormControl('', [Validators.pattern('^[0][7][0-9]{8}$')]),
      age: new FormControl('', [Validators.min(14), Validators.pattern('[1-9][0-9]')]),
      domains:  new FormControl(''),
      volunteered:  new FormControl(''),
      description: new FormControl(''),
      address: new FormControl(''),
      logo: new FormControl(''),
      website: new FormControl(''),
    })
  }

  ngOnInit(): void {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
  )}

  uploadFile(event: any) {
    const file = (event.target as HTMLInputElement).files![0];
    this.form.patchValue({
      file: file,
    });
    this.form.get('logo')!.updateValueAndValidity();
  }

  onSubmit(formDirective: FormGroupDirective) {
    if (this.logedin_user_role == 'USER'){
      this.volunteerService.update(this.oldName, this.form.value).subscribe({
        next: () => {
          this._snackBar.open('Successfull updated your account!', 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
        },
        error: (e: { error: { message: string; }; }) => {
          this._snackBar.open(e.error.message, 'OK', {
          duration: 10000,
          panelClass: 'fail-snackbar'
        })
        }
      })
    }
    else if (this.logedin_user_role == 'ORGANIZATION'){
      let formData: any = new FormData();
      let org: Partial<Organization> = this.form.value as Partial<Organization>;
      org.logo = undefined;
      formData.append('organization', JSON.stringify(org));
      formData.append('file', this.form.controls['logo'].value);
      console.log( JSON.stringify(org))
      this.orgService.update(formData, this.oldName).subscribe({
        next: () => {
          this._snackBar.open('Successfully updated your account!', 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
        },
        error: (e: { error: { message: string; }; }) => {
            this._snackBar.open(e.error.message, 'OK', {
              duration: 10000,
              panelClass: 'fail-snackbar'
            })
          }
        });
    }


  }
}
