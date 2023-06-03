import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { UserModule } from './user/user.module';
import { HeaderComponent } from './core/components/header/header.component';
import { DomainModule } from './domain/domain.module';
import { ProjectModule } from './project/project.module';
import { ProfileModule } from './profile/profile.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HomeComponent } from './home/component/home.component';
import { HomeModule } from './home/home.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    UserModule,
    DomainModule,
    ProjectModule,
    HomeModule,
    ProfileModule,
    BrowserAnimationsModule,
    MatIconModule,  
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
