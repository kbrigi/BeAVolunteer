import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VolunteerSetComponent } from './volunteer-set.component';

describe('VolunteerSetComponent', () => {
  let component: VolunteerSetComponent;
  let fixture: ComponentFixture<VolunteerSetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VolunteerSetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VolunteerSetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
