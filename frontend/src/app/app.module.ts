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
    ProfileModule, 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
