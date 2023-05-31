import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { Observable } from 'rxjs';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { Organization } from 'src/app/user/models/organization.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrgService {

  constructor(private service: BackendService) { }  

  registration(formData: FormData): Observable<void> {
    return this.service.post(`${environment.apiUrl}/org`, formData);
  }

  update(formData: FormData, name: String): Observable<void> {
    return this.service.post(`${environment.apiUrl}/org/update/${name}`, formData);
  }

  getAll(): Observable<Organization[]> {
    return this.service.get(`${environment.apiUrl}/org/all`);
  }

  getOrg(name: String): Observable<Organization> {
    return this.service.get(`${environment.apiUrl}/org/${name}`);
  }

  delete(username: String): Observable<void> {
    return this.service.delete(`${environment.apiUrl}/org/${username}`);
  }
}
