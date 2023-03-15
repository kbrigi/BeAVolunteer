import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BackendService } from 'src/app/core/backend/backend.service';
import { Organization } from 'src/app/features/models/organization.model';
import { Volunteer } from 'src/app/features/models/volunteer.model';
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
