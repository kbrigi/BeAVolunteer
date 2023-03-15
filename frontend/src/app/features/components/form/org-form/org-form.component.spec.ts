import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrgFormComponent } from './org-form.component';

describe('OrgFormComponent', () => {
  let component: OrgFormComponent;
  let fixture: ComponentFixture<OrgFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrgFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrgFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
