import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card'
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DomainComponent } from './features/components/domain/domain.component';
import { CoreModule } from './core/core.module';
import { VolunteerFormComponent } from './features/components/form/volunteer-form/volunteer-form.component';
import { OrgFormComponent } from './features/components/form/org-form/org-form.component';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { ProjFormComponent } from './features/components/form/proj-form/proj-form.component';
import { LoginFormComponent } from './features/components/form/login-form/login-form.component';

@NgModule({
  declarations: [
    AppComponent,
    DomainComponent,
    VolunteerFormComponent,
    OrgFormComponent,
    ProjFormComponent,
    LoginFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    MatCardModule,
    HttpClientModule,
    FormsModule,
    MatIconModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatCheckboxModule,
    MatSnackBarModule,
    NgxMatFileInputModule,
    MatDatepickerModule,
    MatNativeDateModule 
  ],
  exports: [VolunteerFormComponent],
  providers: [MatDatepickerModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
