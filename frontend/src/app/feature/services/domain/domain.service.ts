import { Injectable } from '@angular/core';
import { BackendService } from 'src/app/core/services/backend/backend.service';
import { Domain } from '../../models/domain.model';
import { Observable } from "rxjs";
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DomainService {
  domains: Domain[] = [];

  constructor(private service: BackendService) { }

  getAllDomains(): Observable<Domain[]> {
    return this.service.get(`${environment.apiUrl}/domain`)
  }
}
