import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjDetailsComponent } from './proj-details.component';

describe('ProjDetailsComponent', () => {
  let component: ProjDetailsComponent;
  let fixture: ComponentFixture<ProjDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
