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
import { DomainComponent } from './feature/components/domain/domain.component';
import { CoreModule } from './core/core.module';
import { VolunteerFormComponent } from './feature/components/form/volunteer-form/volunteer-form.component';
import { OrgFormComponent } from './feature/components/form/org-form/org-form.component';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { ProjFormComponent } from './feature/components/form/proj-form/proj-form.component';
import { LoginFormComponent } from './feature/components/form/login-form/login-form.component';
import { FeatureModule } from './feature/feature.module';
import { HeaderComponent } from './core/components/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    FeatureModule, 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
