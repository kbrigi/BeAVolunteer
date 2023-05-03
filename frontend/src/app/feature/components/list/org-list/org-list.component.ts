import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Organization } from 'src/app/feature/models/organization.model';
import { OrgService } from 'src/app/feature/services/user/org/org.service';

@Component({
  selector: 'app-org-list',
  templateUrl: './org-list.component.html',
  styleUrls: ['./org-list.component.css']
})
export class OrgListComponent implements OnInit {
  orgs: Organization[] = [];

  constructor(private orgService: OrgService, private router: Router){}

  getAllOrgs(): void {
    this.orgService.getAll().subscribe(result => {
      this.orgs = result;
    })
  }

  ngOnInit(): void {
    this.getAllOrgs();
  }

  navigate(username: String): void {
    console.log("/org/"+username);
    this.router.navigate(["/org"], { queryParams: {name: username}});
  }

}
