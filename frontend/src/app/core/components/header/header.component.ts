import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
// import { LoginService } from 'src/app/user/services/user/login/user.service';
import jwt_decode from 'jwt-decode';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/internal/Observable';
import { UserService } from 'src/app/user/services/user/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  logedin: boolean = false;
  logedin_user_role: String = '';
  logedin_user_name: String = '';
  myControl = new FormControl<string>('');
  options: string[] = [];
  filteredOptions: Observable<string[]> | undefined;
  constructor(private userService: UserService,  private _snackBar: MatSnackBar){}

  ngOnInit(): void {
    this.userService.currentLoginState.subscribe(result => this.logedin = result);
    this.getRole();
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        this.logedin_user_name = token.sub;
        this.userService.getRole(this.logedin_user_name).subscribe(result => {
          this.logedin_user_role = result.role;
          console.log(result.role)
        }
        );
      }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.logedin_user_role = '';
    this.logedin_user_name = '';
    this.userService.loginState.next(false);
    this._snackBar.open("You successfully logged out!", 'OK', {
      duration: 10000,
      panelClass: 'success-snackbar'
    });
  }

}


