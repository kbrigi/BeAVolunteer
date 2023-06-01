import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './user/components/form/login-form/login-form.component';
import { OrgFormComponent } from './user/components/form/org-form/org-form.component';
import { ProjFormComponent } from './project/components/proj-form/proj-form.component';
import { VolunteerFormComponent } from './user/components/form/volunteer-form/volunteer-form.component';
import { DomainFormComponent } from './domain/components/domain-form/domain-form.component';
import { ProjListComponent } from './project/components/proj-list/proj-list.component';
import { OrgListComponent } from './user/components/list/org-list/org-list.component';
import { OrgDetailsComponent } from './user/components/details/org-details/org-details.component';
import { ProjDetailsComponent } from './project/components/proj-details/proj-details.component';
import { ProfilePageComponent } from './profile/components/profile-page/profile-page.component';

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
    path: 'save/domain',
    component: DomainFormComponent,
    pathMatch: 'full'
  },
  {
    path: 'save/project',
    component: ProjFormComponent,
    pathMatch: 'full'
  },  
  {
    path: 'projects',
    component: ProjListComponent,
  },
  {
    path: 'projects/:domain',
    component: ProjListComponent,
    pathMatch: 'full'
  },
  {
    path: 'project/:name',
    component: ProjDetailsComponent,
    pathMatch: 'full'
  },
  {
    path: 'project/:search',
    component: ProjDetailsComponent,
    pathMatch: 'full'
  },
  {
    path: 'orgs',
    component: OrgListComponent,
    pathMatch:  'full'
  },
  {
    path: 'org/:name',
    component: OrgDetailsComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginFormComponent,
    pathMatch: 'full'
  },
  {
    path: 'profile/:page/:name',
    component: ProfilePageComponent,
    pathMatch: 'full'
  },
  {
    path: 'profile/:page',
    component: ProfilePageComponent,
    pathMatch: 'full'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
