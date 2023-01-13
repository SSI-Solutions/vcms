import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUiConnectionsComponent } from './admin-ui-connections.component';

describe('AdminUiConnectionsComponent', () => {
  let component: AdminUiConnectionsComponent;
  let fixture: ComponentFixture<AdminUiConnectionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminUiConnectionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUiConnectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
