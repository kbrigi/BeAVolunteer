import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/user/services/user/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {  
  loginForm: FormGroup;

  constructor(private userService: UserService, private _snackBar: MatSnackBar) {
    this.loginForm = this.ngOnInit()
  }

  ngOnInit(): FormGroup {
    const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
  
    return new FormGroup({
    email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    })
  }

  login(formDirective: FormGroupDirective) {
    this.userService.login(this.loginForm.value).subscribe({
      next: (result: any) => {
          console.log(result);
          localStorage.setItem('token', JSON.stringify(result));
          this.userService.loginState.next(true);
          // @ts-ignore
          this.userService.logedinUsername.next(jwt_decode(JSON.stringify(result)).sub);
          this._snackBar.open("Hello! You were successfully logged in!", 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
      },
      // this.router.navigate(["/home"]);
      error: (e: { error: { message: string; }; }) => { 
      this._snackBar.open(e.error.message, 'OK', {
        duration: 10000,
        panelClass: 'fail-snackbar'
      })
      console.log(e);
    }
    
  }
    
  );

  this.loginForm.reset()
  formDirective.resetForm()
  }

}

