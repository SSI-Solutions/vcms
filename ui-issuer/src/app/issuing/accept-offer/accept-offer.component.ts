import {Component, OnDestroy, OnInit} from '@angular/core';
import {IssuingService} from '../../../../build/generated/api-issuer/api/issuing.service';
import {IssuingRequest} from '../../../../build/generated/api-issuer/model/issuingRequest';
import {SharedDataService} from '../service/shared-data.service';
import {Subscription} from 'rxjs';
import {IssuingStatePollingService} from '../service/issuing-state-polling.service';
import {Router} from '@angular/router';
import {EnvConfig, EnvironmentLoaderService} from '../../config/environment-loader.service';


enum Progress {
	start = 0,
	offerAccepted = 1,
	vcIssued = 2
}

@Component({
	selector: 'app-accept-offer',
	templateUrl: './accept-offer.component.html',
	styleUrls: ['./accept-offer.component.scss']
})
export class AcceptOfferComponent implements OnInit, OnDestroy {

	progress: Progress = Progress.start;
	protected envConfig!: EnvConfig;
	private pollingSubscription: Subscription;
	private readonly issuingRequest: IssuingRequest;

	constructor(
		private issuingService: IssuingService, private sharedDataService: SharedDataService,
		private pollingService: IssuingStatePollingService, private router: Router,
		private readonly envService: EnvironmentLoaderService
	) {
		this.issuingRequest = {
			credentialDefinitionId: this.sharedDataService.credentialDefinition?.credentialDefinitionId,
			attributes: sharedDataService.dataForm?.value,
			connectionId: sharedDataService.connectionId
		}

		this.pollingSubscription = this.pollingService.pollingInterval.subscribe((issuingState: any) => {
			this.checkIssuingState(issuingState);
		});
	}

	ngOnInit(): void {
		this.envConfig = this.envService.getEnvConfig();
		this.issuingService.configuration.basePath = this.envConfig.issuerBasePath;
		this.issuingService.issueCredential(this.issuingRequest).subscribe(
			data => {
				if (data.processId !== undefined) {
					this.sharedDataService.processId = data.processId;
				}
			}
		);
	}

	ngOnDestroy(): void {
		this.pollingSubscription.unsubscribe();
	}

	private checkIssuingState(issuingState: any): void {
		if (issuingState === 'REQUEST_RECEIVED') {
			this.progress = Progress.offerAccepted;
		}
		if (issuingState === 'VC_ISSUED') {
			this.progress = Progress.vcIssued;
			this.router.navigateByUrl('/result').then();
		}
	}
}
