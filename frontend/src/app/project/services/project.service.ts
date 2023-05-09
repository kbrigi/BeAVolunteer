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

  getProjectsFiltered(paramName: String[], domains_param: String[], owner_param: String|undefined) : Observable<Project[]>  {
    if (owner_param == undefined) {
    console.log(`${environment.apiUrl}/proj/filter?${paramName[0]}=${domains_param}`)
      return this.service.get(`${environment.apiUrl}/proj/filter?${paramName[0]}=${domains_param}`);
    }
    console.log(`${environment.apiUrl}/proj/filter?${paramName[0]}=${domains_param}&${paramName[1]}=${owner_param}`)
    return this.service.get(`${environment.apiUrl}/proj/filter?${paramName[0]}=${domains_param}&${paramName[1]}=${owner_param}`);
    
  }

  delete(username: String): Observable<void> {
    return this.service.delete(`${environment.apiUrl}/proj/${username}`);
  }


}
