import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyCredentialComponent } from './verify-credential.component';

describe('VerifyCredentialComponent', () => {
  let component: VerifyCredentialComponent;
  let fixture: ComponentFixture<VerifyCredentialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerifyCredentialComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyCredentialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
