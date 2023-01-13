import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUiHoldersComponent } from './admin-ui-holders.component';

describe('AdminUiHoldersComponent', () => {
  let component: AdminUiHoldersComponent;
  let fixture: ComponentFixture<AdminUiHoldersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminUiHoldersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUiHoldersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
