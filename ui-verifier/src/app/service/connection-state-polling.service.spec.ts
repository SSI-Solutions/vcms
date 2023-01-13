import { TestBed } from '@angular/core/testing';

import { ConnectionStatePollingService } from './connection-state-polling.service';

describe('ConnectionStatePollingService', () => {
  let service: ConnectionStatePollingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConnectionStatePollingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
