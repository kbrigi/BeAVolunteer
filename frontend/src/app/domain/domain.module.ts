import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { DomainComponent } from './components/domain/domain.component';
import { DomainService } from './services/domain.service';
import { DomainFormComponent } from './components/domain-form/domain-form.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';



@NgModule({
  declarations: [
    DomainFormComponent,
    DomainComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatCardModule,
    NgxMatFileInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule
  ],
  providers: [
    DomainComponent,
    DomainService
  ]
})
export class DomainModule { }
