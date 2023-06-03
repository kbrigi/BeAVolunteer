import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectService } from 'src/app/project/services/project.service';
import { OrgService } from 'src/app/user/services/org/org.service';
import { UserService } from 'src/app/user/services/user/user.service';
import { VolunteerService } from 'src/app/user/services/volunteer/volunteer.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  projectsCount = 0
  orgsCount = 0
  volunteersCount = 0


  constructor(private router: Router, private projectService: ProjectService,
    private volService: VolunteerService, private orgService: OrgService){}
  
  ngOnInit(): void {
    this.projectService.getNr().subscribe(result => this.projectsCount = result)
    this.orgService.getNr().subscribe(result => this.orgsCount = result)
    this.volService.getNr().subscribe(result => this.volunteersCount = result)

    
  }


  redirect(where: String):void {
    this.router.navigate(['/registration/'+where])
  }

}
