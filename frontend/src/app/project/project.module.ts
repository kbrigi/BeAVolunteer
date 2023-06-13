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
import { ProjDetailsComponent } from './components/proj-details/proj-details.component';
import { RouterModule } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { DomainModule } from '../domain/domain.module';
import { DomainFormComponent } from './components/domain-form/domain-form.component';

@NgModule({
  declarations: [
    ProjFormComponent,
    ProjListComponent,
    ProjDetailsComponent,
    DomainFormComponent
  ],
  imports: [
    CommonModule,
    DomainModule,
    MatFormFieldModule,
    MatCardModule,
    NgxMatFileInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    RouterModule,
    MatGridListModule,
    MatCheckboxModule,
    MatRadioModule,
    MatMenuModule,
    MatTooltipModule,
    MatButtonModule,
    MatButtonToggleModule
  ],
  exports: [
    ProjFormComponent
  ],
  providers: [
    ProjectService
  ]
})
export class ProjectModule { }
