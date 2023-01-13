import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevokedComponent } from './revoked.component';

describe('RevokedComponent', () => {
  let component: RevokedComponent;
  let fixture: ComponentFixture<RevokedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RevokedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevokedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
