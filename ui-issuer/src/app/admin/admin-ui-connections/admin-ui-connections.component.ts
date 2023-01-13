import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatSnackBar} from '@angular/material/snack-bar';
import {CredentialResponse, HolderResponse} from 'build/generated/api-issuer/model/models';
import {AdminService} from 'build/generated/api-issuer/api/admin.service';
import {EnvConfig, EnvironmentLoaderService} from "../../config/environment-loader.service";
import {SharedDataService} from "../../issuing/service/shared-data.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-ui-connections',
  templateUrl: './admin-ui-connections.component.html',
  styleUrls: ['./admin-ui-connections.component.scss']
})
export class AdminUiConnectionsComponent implements OnInit, AfterViewInit {

	holder: HolderResponse | undefined;
	displayedColumns: string[] = ['connectionId', 'credentialId', 'processState', 'revocationState', 'issuingDate', 'revokeBtn'];
	credentials = new MatTableDataSource<CredentialResponse>();

	@ViewChild(MatSort) sort!: MatSort;
	@ViewChild(MatPaginator) paginator!: MatPaginator;
	protected envConfig!: EnvConfig;

	constructor(
		private adminService: AdminService, private sharedDataService: SharedDataService, private router: Router,
		private readonly envService: EnvironmentLoaderService, private snackbar: MatSnackBar,
	) {
	}

	ngOnInit() {
		this.envConfig = this.envService.getEnvConfig();
		this.adminService.configuration.basePath = this.envConfig.issuerBasePath;
		this.holder = this.sharedDataService.holder;

		if (this.holder?.id != undefined) {
			this.adminService.getCredentialsFromHolder(this.holder.id).subscribe(data => {
				this.credentials.data = data;
			});
		}
	}

	ngAfterViewInit(): void {
		this.credentials.sort = this.sort;
		this.credentials.paginator = this.paginator;
	}

	doFilter(value: string): void {
		this.credentials.filter = value.trim().toLocaleLowerCase();
	}

	revokeCredential(credentialId: string): void {
		this.adminService.revokeCredential(credentialId).subscribe(
			async () => {
				await this.updateRevocationStatus()
			},
			error => {
				this.snackbar.open(`${error.error.error} (Status code: ${error.error.status})`, 'Dismiss', {
					duration: 3000,
					verticalPosition: 'bottom',
					panelClass: ['snackbar-error'],
				});
			},
		);
	}

	async updateRevocationStatus(): Promise<void> {
		if (this.holder?.id != undefined) {
			this.adminService.getCredentialsFromHolder(this.holder.id).subscribe(data => {
				//Refresh credentials data
				this.credentials.data = data;
			});
		}
	}

	getHolderId(): string |undefined {
		return this.holder?.userId;
	}
}
