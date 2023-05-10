import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { MatIconModule } from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [    
    ProfilePageComponent
  ],
  imports: [
    CommonModule,    
    MatIconModule,    
    MatListModule,
    RouterModule
  ]
})
export class ProfileModule { }
