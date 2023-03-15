import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { BackendService } from 'src/app/core/backend/backend.service';
import { environment } from 'src/environments/environment';
import { Volunteer } from '../../../models/volunteer.model';

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {

  constructor(private service: BackendService) { }  

  registration(volunteer: Volunteer): Observable<void> {
    console.log(volunteer, `${environment.apiUrl}/volunteer`);
    return this.service.post(`${environment.apiUrl}/volunteer`, volunteer);
  }
}
