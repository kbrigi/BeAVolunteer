import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { User } from 'src/app/user/models/user.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  loginState = new BehaviorSubject(!!localStorage.getItem('token'));
  currentLoginState = this.loginState.asObservable();

  constructor(private service: BackendService) {
  }

  login(user: User): Observable<void> {
    return this.service.post(`${environment.apiUrl}/login`, user)
  }

  getRole(username: String): Observable<User> {
    return this.service.get(`${environment.apiUrl}/role/${username}`)
  }
}
