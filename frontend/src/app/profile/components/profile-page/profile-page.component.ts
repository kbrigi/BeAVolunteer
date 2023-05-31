import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import jwt_decode from 'jwt-decode';
import { UserService } from 'src/app/user/services/user/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  logedin_user_role: String = '';
  logedin_user_name: String = '';
  tab: String = ''
  tabs = [
    { name: 'Account', icon: 'account_circle'},
    { name: 'Favourites', icon: 'favorite'},
    { name: 'Own projects', icon: 'article'}
  ]
  
  constructor(private fb: FormBuilder, private activatedRoute: ActivatedRoute, private userService: UserService){
    this.activatedRoute.params.subscribe((param) => {
      this.tab = param['page'],
      this.logedin_user_name = param['name']
      console.log(param['page'])
    }) 

    //  
    // console.log(localStorage.getItem('token'))
    // if (localStorage.getItem('token') !== null) {
    //     const token = jwt_decode(localStorage.getItem('token')!);
    //     // @ts-ignore
    //     this.logedin_user_name = token.sub;

    //     this.userService.getRole(this.logedin_user_name).subscribe(result => {
    //       this.logedin_user_role = result.role;
    //       console.log(result.role)
    //     }
    //     );
    //   }  
  }

  ngOnInit(): void {  
    this.getRole() 
    
  }

  getRole(): void {
    if (localStorage.getItem('token') !== null) {
        const token = jwt_decode(localStorage.getItem('token')!);
        // @ts-ignore
        this.logedin_user_name = token.sub;

        this.userService.getRole(this.logedin_user_name).subscribe(result => {
          this.logedin_user_role = result.role;
        }
        );
      }
  }

}
