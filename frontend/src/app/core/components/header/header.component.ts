import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from 'src/app/feature/services/user/login/login.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  logedin: boolean = false;
  logedin_user_role: String = '';
  constructor(private loginService: LoginService,  private _snackBar: MatSnackBar, private userService: LoginService){}

  ngOnInit(): void {
    this.loginService.currentLoginState.subscribe(result => this.logedin = result);
    this.getRole();
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        const name = token.sub;
        this.userService.getRole(name).subscribe(result => {
          this.logedin_user_role = result.role;
          console.log(result.role)
        }
        );
      }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.loginService.loginState.next(false);
    this._snackBar.open("You successfully logged out!", 'OK', {
      duration: 10000,
      panelClass: 'success-snackbar'
    });
  }

}


