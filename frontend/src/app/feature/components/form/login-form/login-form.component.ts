import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from 'src/app/feature/services/user/login/login.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {  

  loginForm: FormGroup;
  
  constructor(private loginService: LoginService, private _snackBar: MatSnackBar) {
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
    this.loginService.login(this.loginForm.value).subscribe({
      next: (result: any) => {
          console.log(result);
          localStorage.setItem('token', JSON.stringify(result));
          this.loginService.loginState.next(true);
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
