import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {SharedDataService} from '../service/shared-data.service';
import {DefaultService} from '../../../build/generated/api-verifier/api/api';
import {VerStatus} from '../../../build/generated/api-verifier/model/verStatus';
import {VerifyStatePollingService} from '../service/verify-state-polling.service';
import {Router} from '@angular/router';
import {EnvConfig, EnvironmentLoaderService} from '../config/environment-loader.service';

enum Progress {
	polling = 0,
	proofPresented = 1,
}

class VerificationRequest {
	credentialDefinitionId: string = '';
	attributes: string[] = [];
	connectionId: string = '';
}

@Component({
	selector: 'app-verify-credential',
	templateUrl: './verify-credential.component.html',
	styleUrls: ['./verify-credential.component.scss']
})
export class VerifyCredentialComponent implements OnInit, OnDestroy {

	progress: Progress = Progress.polling;
	protected envConfig!: EnvConfig;
	private pollingSubscription: Subscription;
	private readonly verificationRequest: VerificationRequest;

	constructor(
		private defaultService: DefaultService, private sharedDataService: SharedDataService,
		private pollingService: VerifyStatePollingService, private router: Router,
		private readonly envService: EnvironmentLoaderService
	) {

		this.verificationRequest = {
			credentialDefinitionId: this.sharedDataService.credentialDefinition?.credentialDefinitionId!,
			attributes: this.sharedDataService.selectedClaims,
			connectionId: this.sharedDataService.connectionId
		}

		this.pollingSubscription = this.pollingService.pollingInterval.subscribe((verifyState: VerStatus) => {
			this.checkVerifyState(verifyState);
		});
	}

	ngOnInit(): void {
		this.envConfig = this.envService.getEnvConfig();
		this.defaultService.configuration.basePath =this.envConfig.verifierBasePath;
		this.defaultService.verifyCredentials(this.verificationRequest).subscribe(
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

	private checkVerifyState(verifyState: any): void {
		if (verifyState === VerStatus.Verified) {
			this.progress = Progress.proofPresented;
			this.router.navigateByUrl('/result').then();
		}
		if (verifyState === VerStatus.Revoked) {
			this.progress = Progress.proofPresented;
			this.router.navigateByUrl('/revoked').then();
		}
	}
}
