import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Domain } from 'src/app/domain/models/domain.model';
import { Volunteer } from 'src/app/user/models/volunteer.model';
import { UserService } from 'src/app/user/services/user/user.service';

@Component({
  selector: 'app-volunteer-set',
  templateUrl: './volunteer-set.component.html',
  styleUrls: ['./volunteer-set.component.css']
})
export class VolunteerSetComponent {
  givenData: Volunteer
  domains: Domain[] = [];
  projectForm!: FormGroup;
  oldName: String = '';

  constructor(private activatedRoute: ActivatedRoute, private userService: UserService){
    this.activatedRoute.params.subscribe((param) => {
      this.oldName = param['name']
      this.userService.
    }) 
    }
}
