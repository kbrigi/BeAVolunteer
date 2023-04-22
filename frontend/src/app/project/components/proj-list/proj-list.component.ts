import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-proj-list',
  templateUrl: './proj-list.component.html',
  styleUrls: ['./proj-list.component.css']
})
export class ProjListComponent implements OnInit {
  projects: Project[] = [];

  constructor(private projectService: ProjectService){}

  getAllProjects(): void {
    this.projectService.getAll().subscribe(result => {
      this.projects = result;
      this.projects.forEach(p => {
        p.creator_name = p.owner.user;
      });
      console.log(this.projects);
    });
  }
   

  ngOnInit(): void {
    this.getAllProjects();
  }
}
