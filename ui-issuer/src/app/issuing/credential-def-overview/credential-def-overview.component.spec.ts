import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialDefOverviewComponent } from './credential-def-overview.component';

describe('CredentialDefOverviewComponent', () => {
  let component: CredentialDefOverviewComponent;
  let fixture: ComponentFixture<CredentialDefOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CredentialDefOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CredentialDefOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
