import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from 'src/app/features/models/user.model';
import { LoginService } from 'src/app/features/services/user/login/login.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {  
  
  loginForm = new FormGroup({
  username: new FormControl('', [Validators.required, Validators.minLength(6)]),
  password: new FormControl('', [Validators.required, Validators.minLength(6)]),
})
  constructor(private loginService: LoginService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  login(user: User) {
    this.loginService.login(user).subscribe(
      (result) => {
      console.log(result);
      localStorage.setItem('token', JSON.stringify(result));
      this.loginService.loginState.next(true);
      this._snackBar.open("Hello! You were successfully logged in!", 'OK', {
        duration: 10000,
        panelClass: 'success-snackbar'
      }); 
      // this.router.navigate(["/home"]);
    (e: { error: { message: string; }; }) => {
      this._snackBar.open(e.error.message, 'OK', {
        duration: 10000,
        panelClass: 'fail-snackbar'
      })
      console.log(e);
    };
  })
  };

  this.loginForm.reset()
  formDirective.resetForm()
}
