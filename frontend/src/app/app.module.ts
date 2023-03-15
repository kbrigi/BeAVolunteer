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

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DomainComponent } from './features/components/domain/domain.component';
import { CoreModule } from './core/core.module';
import { VolunteerFormComponent } from './features/components/form/volunteer-form/volunteer-form.component';
import { OrgFormComponent } from './features/components/form/org-form/org-form.component';

@NgModule({
  declarations: [
    AppComponent,
    DomainComponent,
    VolunteerFormComponent,
    OrgFormComponent
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
    MatSnackBarModule
  ],
  exports: [VolunteerFormComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
