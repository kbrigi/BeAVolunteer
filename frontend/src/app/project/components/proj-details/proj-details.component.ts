import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-proj-details',
  templateUrl: './proj-details.component.html',
  styleUrls: ['./proj-details.component.css']
})
export class ProjDetailsComponent implements OnInit{  

  project: Project = {
    project_name: '',
    project_description: '',
    expiration_date: null as any,
    domains: [],
    creation_date: null as any,
    owner: {
      user: '',
      email: '',
      password: '',
      role: ''
    },
    project_img: ''
  };

  constructor(private projectService: ProjectService,  
    private activatedRoute: ActivatedRoute){ }


  ngOnInit(){
    this.activatedRoute.params.subscribe((param) => {
      this.projectService.getProj(param['name']).subscribe(result => {
        this.project =  result;
        console.log(this.project)
      })
    })
  }
}
