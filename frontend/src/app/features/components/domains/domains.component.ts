import { Component, OnInit } from '@angular/core';
import { Domain } from '../../models/domain.model';
import { DomainService } from '../../services/domain/domain.service';
// import { MatCardModule } from '@angular/material/card'

@Component({
  selector: 'app-domains',
  templateUrl: './domains.component.html',
  styleUrls: ['./domains.component.css']
})
export class DomainsComponent implements OnInit {

  domains: Domain[] = [];

  constructor(private domainService: DomainService) { }

  getAllDomains() {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    )
  }

  ngOnInit(): void {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    )
    alert(this.domains);
  }
}
