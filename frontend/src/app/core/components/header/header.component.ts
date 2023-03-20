import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from 'src/app/feature/services/user/login/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  logedin: boolean = false;

  constructor(private loginService: LoginService,  private _snackBar: MatSnackBar){}

  ngOnInit(): void {
    this.loginService.currentLoginState.subscribe(result => this.logedin = result);
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
