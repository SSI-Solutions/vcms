import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {DefaultService} from '../../../../build/generated/connector-client/api/default.service';
import {ConnectionStatePollingService} from '../service/connection-state-polling.service';
import {ConnStatus} from '../../../../build/generated/connector-client/model/connStatus';
import {SharedDataService} from '../service/shared-data.service';
import {EnvConfig, EnvironmentLoaderService} from '../../config/environment-loader.service';


@Component({
	selector: 'app-establish-connection',
	templateUrl: './establish-connection.component.html',
	styleUrls: ['./establish-connection.component.scss']
})
export class EstablishConnectionComponent implements OnInit, OnDestroy {

	protected envConfig!: EnvConfig;

	qrCode: string = '';
	displayError: boolean = false;
	errorMessage: string = '';
	private pollingSubscription: Subscription;

	constructor(private connectorService: DefaultService, private statePollingService: ConnectionStatePollingService,
		private sharedDataService: SharedDataService, private readonly envService: EnvironmentLoaderService
	) {
		this.pollingSubscription = this.statePollingService.pollingInterval
			.subscribe((connectionState: ConnStatus) => this.statePollingService.routeUser(connectionState));
	}

	ngOnInit(): void {
		this.envConfig = this.envService.getEnvConfig();
		this.connectorService.configuration.basePath = this.envConfig.connectorBasePath;

		this.connectorService.connectionInvitation().subscribe(
			data => {
				if (data.invitationUrl !== undefined && data.connectionId !== undefined) {
					this.qrCode = data.invitationUrl;
					this.sharedDataService.connectionId = data.connectionId;
				}
			},
			err => {
				this.displayError = true;
				this.errorMessage = err.error.message;
			}
		)
	}

	ngOnDestroy(): void {
		this.pollingSubscription.unsubscribe();
	}
}
