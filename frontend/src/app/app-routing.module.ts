import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './feature/components/form/login-form/login-form.component';
import { OrgFormComponent } from './feature/components/form/org-form/org-form.component';
import { ProjFormComponent } from './feature/components/form/proj-form/proj-form.component';
import { VolunteerFormComponent } from './feature/components/form/volunteer-form/volunteer-form.component';

const routes: Routes = [
  {
    path: 'registration/user',
    component: VolunteerFormComponent,
    pathMatch: 'full'
  },
  {
    path: 'registration/org',
    component: OrgFormComponent,
    pathMatch: 'full'
  },
  {
    path: 'save/project',
    component: ProjFormComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginFormComponent,
    pathMatch: 'full'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
