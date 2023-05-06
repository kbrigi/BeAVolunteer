import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { environment } from 'src/environments/environment';
import { Project } from '../models/project.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  constructor(private service: BackendService) { }  
  
  save(formData: FormData) : Observable<void> {
    return this.service.post(`${environment.apiUrl}/proj/save`, formData);
  }

  getAll() : Observable<Project[]> {
    return this.service.get(`${environment.apiUrl}/proj/all`);
  }
  
  getProjectsByOwner() : Observable<Project[]>{
    return this.service.get(`${environment.apiUrl}/proj/owned`);
  }

  getProjectsByDomain(domain: String): Observable<Project[]> {
    return this.service.get(`${environment.apiUrl}/proj/domain/${domain}`);
  }

  getProj(name: String) : Observable<Project> {
    return this.service.get(`${environment.apiUrl}/proj?name=${name}`);
  }

  delete(username: String): Observable<void> {
    return this.service.delete(`${environment.apiUrl}/proj/${username}`);
  }

}
