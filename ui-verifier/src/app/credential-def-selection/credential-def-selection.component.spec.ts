import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialDefSelectionComponent } from './credential-def-selection.component';

describe('CredentialDefSelectionComponent', () => {
  let component: CredentialDefSelectionComponent;
  let fixture: ComponentFixture<CredentialDefSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CredentialDefSelectionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CredentialDefSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
