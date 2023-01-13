import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HolderResponse} from 'build/generated/api-issuer/model/models';
import {AdminService} from '../../../../build/generated/api-issuer/api/admin.service';
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {EnvConfig, EnvironmentLoaderService} from "../../config/environment-loader.service";
import {SharedDataService} from "../../issuing/service/shared-data.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-ui-holders',
  templateUrl: './admin-ui-holders.component.html',
  styleUrls: ['./admin-ui-holders.component.scss']
})
export class AdminUiHoldersComponent implements OnInit, AfterViewInit {

	displayedColumns: string[] = ['id', 'openBtn', 'deleteBtn'];
	holders = new MatTableDataSource<HolderResponse>();

	@ViewChild(MatSort) sort!: MatSort;
	@ViewChild(MatPaginator) paginator!: MatPaginator;
	protected envConfig!: EnvConfig;

	constructor(
		private adminService: AdminService, private sharedDataService: SharedDataService, private router: Router,
		private readonly envService: EnvironmentLoaderService) {
	}

	ngOnInit() {
		this.envConfig = this.envService.getEnvConfig();
		this.adminService.configuration.basePath = this.envConfig.issuerBasePath;
		this.adminService.getHolders().subscribe(data => {
			this.holders.data = data;
		});
	}

	ngAfterViewInit(): void {
		this.holders.sort = this.sort;
		this.holders.paginator = this.paginator;
	}

	doFilter(value: string): void {
		this.holders.filter = value.trim().toLocaleLowerCase();
	}

	deleteHolder(holderId: string): void {
		this.adminService.deleteHolder(holderId).subscribe();
		window.location.reload();
	}

	openCredentials(holder: HolderResponse): void {
		this.sharedDataService.holder = holder;
		if (holder.userId != null) {
			this.router.navigate(["admin", holder.userId]).then();
		}
	}
}
