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

  getProjectsFiltered(paramName: String[], domains_param: String[]|undefined, 
    owner_param: String|undefined, org_param: String[]|undefined) : Observable<Project[]>  {
      let url = `${environment.apiUrl}/proj/filter?`
      let not_first = false
      if (domains_param != undefined) {
        url += `${paramName[0]}=${domains_param}`
        not_first = true
      }
      if (owner_param != undefined) {
        if (not_first == true) {
          url += `&${paramName[1]}=${owner_param}`
        }
        else {
          url+=`${paramName[1]}=${owner_param}`
          not_first = true
        }
      }
      if (org_param != undefined) {
        if (not_first == true) {
          url += `&${paramName[2]}=${org_param}`
        }
        else {
          url+=`${paramName[2]}=${org_param}`
        }
      }
      console.log(url)
       return this.service.get(url);    
  }

  delete(username: String): Observable<void> {
    return this.service.delete(`${environment.apiUrl}/proj/${username}`);
  }


}
