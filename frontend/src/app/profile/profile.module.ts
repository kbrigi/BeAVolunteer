import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { MatIconModule } from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { ProjectsPageComponent } from './components/projects-page/projects-page.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import {MatDialogModule} from '@angular/material/dialog';
import { ProjectPopUpComponent } from './components/project-pop-up/project-pop-up.component';
import { ProjectModule } from '../project/project.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';

@NgModule({
  declarations: [    
    ProfilePageComponent, 
    ProjectsPageComponent, 
    ProjectPopUpComponent
  ],
  imports: [
    ProjectModule,
    CommonModule,    
    MatIconModule,    
    MatListModule,
    RouterModule,
    MatGridListModule,
    MatCardModule,
    MatDialogModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    NgxMatFileInputModule,
    MatDatepickerModule,
  ]
})
export class ProfileModule { }
