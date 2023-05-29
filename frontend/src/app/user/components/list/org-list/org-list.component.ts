import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Organization } from 'src/app/user/models/organization.model';
import { OrgService } from 'src/app/user/services/org/org.service';
import { UserService } from 'src/app/user/services/user/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-org-list',
  templateUrl: './org-list.component.html',
  styleUrls: ['./org-list.component.css']
})
export class OrgListComponent implements OnInit {
  orgs: Organization[] = [];
  logedin_user_role: String = '';

  constructor(private orgService: OrgService, private userService: UserService,
    private router: Router, private _snackBar: MatSnackBar){
    }

  getAllOrgs(): void {
    this.orgService.getAll().subscribe(result => {
      this.orgs = result;
    })
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        const name = token.sub;
        this.userService.getRole(name).subscribe(result => {
          this.logedin_user_role = result.role;
        }
        );
      }
  }

  ngOnInit(): void {
    this.getAllOrgs();
    this.getRole();
  }

  clickDelete(username: String): void {
    console.log("/org/"+username);
    this.orgService.delete(username).subscribe({
      next: (result: any) => {
          this._snackBar.open("You deleted the "+username +" oranization!", 'OK', {
            duration: 10000,
            panelClass: 'success-snackbar'
          });
          this.router.navigate(["/orgs"]);
      },
      error: (e: { error: { message: string; }; }) => { 
      this._snackBar.open(e.error.message, 'OK', {
        duration: 10000,
        panelClass: 'fail-snackbar'
      })
      console.log(e);
    }
  });
  }
}
