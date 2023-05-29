import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginFormComponent } from './components/form/login-form/login-form.component';
import { OrgFormComponent } from './components/form/org-form/org-form.component';
import { VolunteerFormComponent } from './components/form/volunteer-form/volunteer-form.component';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatGridListModule} from '@angular/material/grid-list';
import { OrgListComponent } from './components/list/org-list/org-list.component';
import { OrgDetailsComponent } from './components/details/org-details/org-details.component';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { OrgService } from './services/org/org.service';
import { UserService } from './services/user/user.service';
import { VolunteerService } from './services/volunteer/volunteer.service';


@NgModule({
  declarations: [
    VolunteerFormComponent,
    OrgFormComponent,
    LoginFormComponent,
    OrgListComponent,
    OrgDetailsComponent,
  ],
  imports: [
    CommonModule,
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
    MatNativeDateModule,
    RouterModule.forRoot([]),
    MatGridListModule,
    MatButtonModule,
  ],
  exports: [],
  providers: [
      VolunteerService,
      OrgService,
      UserService      
  ],
})
export class UserModule { }
