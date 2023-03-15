import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjFormComponent } from './proj-form.component';

describe('ProjFormComponent', () => {
  let component: ProjFormComponent;
  let fixture: ComponentFixture<ProjFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
