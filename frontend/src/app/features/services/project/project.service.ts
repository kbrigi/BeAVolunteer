import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { BackendService } from 'src/app/core/backend/backend.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  constructor(private service: BackendService) { }  
  
  save(formData: FormData) : Observable<void> {
    return this.service.post(`${environment.apiUrl}/proj/save`, formData);
    
  }

}
