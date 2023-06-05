import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css']
})
export class RegistrationFormComponent {
  tab: String = ''

  constructor(private activatedRoute: ActivatedRoute){
    this.activatedRoute.params.subscribe((param) => {
      this.tab = param['page']
      console.log(param['page'])
    }) 
  }




}
