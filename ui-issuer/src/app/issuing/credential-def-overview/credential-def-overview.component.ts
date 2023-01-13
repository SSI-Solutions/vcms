import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CredentialDefinition } from 'build/generated/api-issuer/model/models';
import { DefaultService } from 'build/generated/api-issuer/api/default.service';
import { SharedDataService } from '../service/shared-data.service';
import { Router } from '@angular/router';
import { EnvConfig, EnvironmentLoaderService } from '../../config/environment-loader.service';

@Component({
	selector: 'app-credential-def-overview',
	templateUrl: './credential-def-overview.component.html',
	styleUrls: ['./credential-def-overview.component.scss']
})
export class CredentialDefOverviewComponent implements OnInit, AfterViewInit {

	displayedColumns: string[] = ['credentialDefinitionId', 'claims', 'select'];
	dataSource = new MatTableDataSource<CredentialDefinition>();
	@ViewChild(MatSort) sort!: MatSort;
	@ViewChild(MatPaginator) paginator!: MatPaginator;
	protected envConfig!: EnvConfig;

	constructor(
		private defaultService: DefaultService, private sharedDataService: SharedDataService, private router: Router,
		private readonly envService: EnvironmentLoaderService
	) {
	}

	ngOnInit(): void {
		this.envConfig = this.envService.getEnvConfig();
		this.defaultService.configuration.basePath = this.envConfig.issuerBasePath;
		this.defaultService.getCredentialDefinitions().subscribe(data => {
			this.dataSource.data = data.filter(row => row.credentialDefinitionId != null && row.claims!.length > 0);
		});
	}

	ngAfterViewInit(): void {
		this.dataSource.sort = this.sort;
		this.dataSource.paginator = this.paginator;
	}

	doFilter(value: string): void {
		this.dataSource.filter = value.trim().toLocaleLowerCase();
	}

	credDefSelected(credentialDefinition: CredentialDefinition) {
		this.sharedDataService.credentialDefinition = credentialDefinition;
		this.router.navigateByUrl('/data').then();
	}

	formatClaims(claims: string[]): string {
		let output = '';
		claims.forEach((claim, index) => {
			if (index == 0) {
				output = output.concat(claim)
			} else {
				output = output.concat(", " + claim)
			}
		})
		return output;
	}
}
