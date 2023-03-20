import { Component, OnInit } from '@angular/core';
import { Domain } from '../../models/domain.model';
import { DomainService } from '../../services/domain/domain.service';

@Component({
  selector: 'app-domain',
  templateUrl: './domain.component.html',
  styleUrls: ['./domain.component.css']
})
export class DomainComponent implements OnInit {

  domains: Domain[] = [];

  constructor(private domainService: DomainService) { }

  getAllDomains() {
    this.domainService.getAllDomains().subscribe((result: Domain[]) => 
      this.domains = result
    )
  }

  ngOnInit(): void {
    this.getAllDomains();
    // alert(this.domains);
  }
}
