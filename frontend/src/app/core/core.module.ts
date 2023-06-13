import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {BackendService} from "./services/backend/backend.service";
import { AuthGuardService } from './services/auth-guard/auth-guard.service';
import { TokenInterceptor } from './services/interceptor/token.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { IgxAvatarModule } from 'igniteui-angular';



@NgModule({
  declarations: [
    // HeaderComponent
  ],
  imports: [
    CommonModule, 
    MatInputModule,
    FormsModule
  ],
  providers: [
    BackendService,
    AuthGuardService,
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
  ],
  exports: [ 
    // HeaderComponent
  ]
})
export class CoreModule { }