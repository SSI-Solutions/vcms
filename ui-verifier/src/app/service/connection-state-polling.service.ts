import {Injectable} from '@angular/core';
import {Observable, repeat, retry, switchMap, timer} from "rxjs";
import {Router} from "@angular/router";
import {DefaultService} from '../../../build/generated/connector-client/api/default.service';
import {ConnStatus} from '../../../build/generated/connector-client/model/connStatus';
import {SharedDataService} from "./shared-data.service";

@Injectable({
  providedIn: 'root'
})
export class ConnectionStatePollingService {

	pollingInterval: Observable<any>;

	//Polls the connection state all 3 seconds to check when the user can be redirected
	constructor(private router: Router, private readonly connectionService: DefaultService, private sharedDataService: SharedDataService) {
		this.pollingInterval =
			timer(3000).pipe(
				switchMap(() => this.connectionService.connectionState(this.sharedDataService.connectionId)),
				retry(3),
				repeat()
			);
	}

	routeUser(connectionState: ConnStatus) {
		if (connectionState === ConnStatus.Responded || connectionState === ConnStatus.Established) {
			this.router.navigateByUrl("/verify").then();
		}
	}
}
