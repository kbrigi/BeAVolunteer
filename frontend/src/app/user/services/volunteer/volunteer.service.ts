import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { environment } from 'src/environments/environment';
import { Volunteer } from '../../models/volunteer.model';

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {

  constructor(private service: BackendService) { }  

  registration(volunteer: Volunteer): Observable<void> {
    return this.service.post(`${environment.apiUrl}/volunteer`, volunteer);
  }

  getNr(): Observable<number> {
    return this.service.get(`${environment.apiUrl}/user/nr`);
  }

  getByName(username: String): Observable<Volunteer> {
    return this.service.get(`${environment.apiUrl}/user/${username}`)
  }

  update(username: String, volunteer: Volunteer): Observable<void> {
    return this.service.post(`${environment.apiUrl}/update/vol/${username}`, volunteer);
  }
}
