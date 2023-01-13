import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { CredentialDefOverviewComponent } from './issuing/credential-def-overview/credential-def-overview.component';
import { DataFormComponent } from './issuing/data-form/data-form.component';
import { EstablishConnectionComponent } from './issuing/establish-connection/establish-connection.component';
import { AcceptOfferComponent } from './issuing/accept-offer/accept-offer.component';
import { ResultPageComponent } from './issuing/result-page/result-page.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { QRCodeModule } from 'angularx-qrcode';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AdminUiHoldersComponent } from './admin/admin-ui-holders/admin-ui-holders.component';
import { AdminUiConnectionsComponent } from './admin/admin-ui-connections/admin-ui-connections.component';
import { EnvironmentLoaderService } from './config/environment-loader.service';
import { MatSnackBarModule } from "@angular/material/snack-bar";

// AoT requires an exported function for factories
export function httpLoaderFactory(http: HttpClient): TranslateHttpLoader {
	return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

const initAppFn = (envService: EnvironmentLoaderService) => {
	return () => envService.loadEnvConfig('assets/environment-config.json');
};


@NgModule({
	declarations: [
		AppComponent,
		CredentialDefOverviewComponent,
		DataFormComponent,
		EstablishConnectionComponent,
		AcceptOfferComponent,
		ResultPageComponent,
		AdminUiHoldersComponent,
		AdminUiConnectionsComponent,
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		HttpClientModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: httpLoaderFactory,
				deps: [HttpClient]
			}
		}),
		MatTableModule,
		MatPaginatorModule,
		MatSortModule,
		MatSnackBarModule,
		FormsModule,
		ReactiveFormsModule,
		MatFormFieldModule,
		MatInputModule,
		QRCodeModule,
		MatProgressSpinnerModule,
	],
	providers: [
		EnvironmentLoaderService,
		{
			provide: APP_INITIALIZER,
			useFactory: initAppFn,
			multi: true,
			deps: [EnvironmentLoaderService],
		}],
	bootstrap: [AppComponent]
})
export class AppModule {}
