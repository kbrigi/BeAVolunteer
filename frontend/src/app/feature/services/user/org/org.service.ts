import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { Organization } from 'src/app/feature/models/organization.model';
import { Volunteer } from 'src/app/feature/models/volunteer.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrgService {

  constructor(private service: BackendService) { }  

  registration(formData: FormData): Observable<void> {
    return this.service.post(`${environment.apiUrl}/org`, formData);
  }
}
