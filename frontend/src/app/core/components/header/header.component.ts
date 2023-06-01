import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import jwt_decode from 'jwt-decode';
import { FormControl } from '@angular/forms';
import { map, Observable, startWith } from "rxjs";
import { UserService } from 'src/app/user/services/user/user.service';
import { ProjectService } from 'src/app/project/services/project.service';
import { Router } from '@angular/router';
import { Project } from 'src/app/project/models/project.model';

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

  constructor(private userService: UserService,  private _snackBar: MatSnackBar,
    private projectService: ProjectService, private router: Router){}

  ngOnInit(): void {
    this.userService.logedinUsername.subscribe(result => this.logedin_user_name = result);
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

  _filter(myproduct: string): string[] {
    const filterValue = myproduct.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  clickedFn(): void {
    if (this.myControl.value != null) {
      var name: string = this.myControl.value!
      if(name.substring(0, 1) === " ")
      name = name.trimStart();
      this.router.navigate(['/projects/'+name]);
    }
    this.myControl.setValue(' ');
  
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


