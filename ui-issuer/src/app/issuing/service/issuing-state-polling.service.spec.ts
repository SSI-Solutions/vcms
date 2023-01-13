import { TestBed } from '@angular/core/testing';

import { IssuingStatePollingService } from './issuing-state-polling.service';

describe('IssuingStatePollingService', () => {
  let service: IssuingStatePollingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IssuingStatePollingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
