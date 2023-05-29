import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrgSetComponent } from './org-set.component';

describe('OrgSetComponent', () => {
  let component: OrgSetComponent;
  let fixture: ComponentFixture<OrgSetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrgSetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrgSetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
