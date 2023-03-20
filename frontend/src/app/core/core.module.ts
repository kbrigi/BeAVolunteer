import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {BackendService} from "./services/backend/backend.service";
import { AuthGuardService } from './services/auth-guard/auth-guard.service';
import { TokenInterceptor } from './services/interceptor/token.interceptor';
import { HeaderComponent } from './components/header/header.component';



@NgModule({
  declarations: [
    // HeaderComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    BackendService,
    AuthGuardService,
    TokenInterceptor,
  ]
})
export class CoreModule { }
