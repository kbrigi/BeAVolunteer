import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './component/home.component';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    RouterModule,
    MatButtonModule

  ]
})
export class HomeModule { }
