import { Injectable } from '@angular/core';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { Observable } from "rxjs";
import { environment } from 'src/environments/environment';
import { Domain } from '../models/domain.model';

@Injectable({
  providedIn: 'root'
})
export class DomainService {
  domains: Domain[] = [];

  constructor(private service: BackendService) { }

  getAllDomains(): Observable<Domain[]> {
    return this.service.get(`${environment.apiUrl}/domain`)
  }

  save(formData: FormData) : Observable<void> {
    return this.service.post(`${environment.apiUrl}/domain/save`, formData);
  }
}
