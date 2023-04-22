import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectService } from './services/project.service';
import { ProjFormComponent } from './components/proj-form/proj-form.component';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { ProjListComponent } from './components/proj-list/proj-list.component';



@NgModule({
  declarations: [
    ProjFormComponent,
    ProjListComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatCardModule,
    NgxMatFileInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
  ],
  providers: [
    ProjectService
  ]
})
export class ProjectModule { }
