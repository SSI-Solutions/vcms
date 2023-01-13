import {Injectable} from '@angular/core';
import {Observable, repeat, retry, switchMap, timer} from "rxjs";
import {Router} from "@angular/router";
import {SharedDataService} from "./shared-data.service";
import {DefaultService} from 'build/generated/api-verifier/api/api';

@Injectable({
  providedIn: 'root'
})
export class VerifyStatePollingService {

	pollingInterval: Observable<any>;

	//Polls the verifying state all 3 seconds to check when the user can be redirected
	constructor(private router: Router, private readonly verifyingService: DefaultService, private sharedDataService: SharedDataService) {
		this.pollingInterval =
			timer(3000).pipe(
				switchMap(() => this.verifyingService.getVerificationState(this.sharedDataService.processId)),
				retry(3),
				repeat()
			);
	}
}
