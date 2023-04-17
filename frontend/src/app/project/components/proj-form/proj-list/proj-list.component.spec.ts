import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjListComponent } from './proj-list.component';

describe('ProjListComponent', () => {
  let component: ProjListComponent;
  let fixture: ComponentFixture<ProjListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
