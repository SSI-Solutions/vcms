import {Injectable} from '@angular/core';
import {Observable, repeat, retry, switchMap, timer} from "rxjs";
import {Router} from "@angular/router";
import {SharedDataService} from "./shared-data.service";
import {DefaultService} from 'build/generated/api-issuer/api/api';

@Injectable({
  providedIn: 'root'
})
export class IssuingStatePollingService {

	pollingInterval: Observable<any>;

	//Polls the issuing state all 3 seconds to check when the user can be redirected
	constructor(private router: Router, private readonly issuingService: DefaultService, private sharedDataService: SharedDataService) {
		this.pollingInterval =
			timer(3000).pipe(
				switchMap(() => this.issuingService.issuingState(this.sharedDataService.processId)),
				retry(3),
				repeat()
			);
	}
}
