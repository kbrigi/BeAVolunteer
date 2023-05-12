import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  tabs = [
    { name: 'Account', icon: 'account_circle'},
    { name: 'Favourite projects', icon: 'favorite'},
    { name: 'Own projects', icon: 'article'}
  ]

  tab: String = ''

  constructor( private activatedRoute: ActivatedRoute){
    this.activatedRoute.params.subscribe((param) => {
      this.tab = param['page']
    })
  }

  ngOnInit(): void {    
  }




}
