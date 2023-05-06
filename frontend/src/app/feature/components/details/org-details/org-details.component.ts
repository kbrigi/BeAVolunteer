import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Organization } from 'src/app/feature/models/organization.model';
import { OrgService } from 'src/app/feature/services/user/org/org.service';

@Component({
  selector: 'app-org-details',
  templateUrl: './org-details.component.html',
  styleUrls: ['./org-details.component.css']
})
export class OrgDetailsComponent implements OnInit {
  org: Organization = {
    email: '',
    user: '',
    password: '',
    description: '',
    address: '',
    phoneNr: '',
    website: '',
    logo: '',
    domains: []
  };

  
  constructor(private orgService: OrgService,  private activatedRoute: ActivatedRoute){
  }

  ngOnInit(){
    this.activatedRoute.params.subscribe((param) => {
      this.orgService.getOrg(param['name']).subscribe(result => {
        this.org =  result;
        console.log(this.org)
      })
    })
  }

}
