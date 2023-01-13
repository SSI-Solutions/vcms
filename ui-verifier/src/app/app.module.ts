import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { CredentialDefOverviewComponent } from './credential-def-overview/credential-def-overview.component';
import { CredentialDefSelectionComponent } from './credential-def-selection/credential-def-selection.component';
import { EstablishConnectionComponent } from './establish-connection/establish-connection.component';
import { VerifyCredentialComponent } from './verify-credential/verify-credential.component';
import { ResultPageComponent } from './result-page/result-page.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatSortModule } from '@angular/material/sort';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ReactiveFormsModule } from '@angular/forms';
import { QRCodeModule } from 'angularx-qrcode';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EnvironmentLoaderService } from './config/environment-loader.service';
import { RevokedComponent } from './revoked/revoked.component';

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
		CredentialDefSelectionComponent,
		EstablishConnectionComponent,
		VerifyCredentialComponent,
		ResultPageComponent,
        RevokedComponent
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
		RouterModule,
		MatTableModule,
		MatPaginatorModule,
		MatInputModule,
		MatSortModule,
		MatCheckboxModule,
		ReactiveFormsModule,
		QRCodeModule,
		MatProgressSpinnerModule
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
