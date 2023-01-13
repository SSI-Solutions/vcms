import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstablishConnectionComponent } from './establish-connection.component';

describe('EstablishConnectionComponent', () => {
  let component: EstablishConnectionComponent;
  let fixture: ComponentFixture<EstablishConnectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstablishConnectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EstablishConnectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
