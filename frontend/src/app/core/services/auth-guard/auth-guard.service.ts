import { Injectable } from '@angular/core';
import { CanActivate, CanActivateFn, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
  
    constructor(private jwtHelper: JwtHelperService, private router: Router) {
    }
  
    canActivate() {
  
      //get the jwt token which are present in the local storage
      const token = localStorage.getItem("token");
  
      //Check if the token is expired or not and if token is expired then redirect to login page and return false
      if (token && !this.jwtHelper.isTokenExpired(token)) {
        return true;
      }
      this.router.navigate(["login"]);
      return false;
    }
  }
