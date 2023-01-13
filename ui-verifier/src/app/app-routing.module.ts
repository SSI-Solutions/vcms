import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CredentialDefOverviewComponent} from "./credential-def-overview/credential-def-overview.component";
import {CredentialDefSelectionComponent} from "./credential-def-selection/credential-def-selection.component";
import {EstablishConnectionComponent} from "./establish-connection/establish-connection.component";
import {ResultPageComponent} from "./result-page/result-page.component";
import {VerifyCredentialComponent} from "./verify-credential/verify-credential.component";
import { RevokedComponent } from './revoked/revoked.component';

const routes: Routes = [
  	{ path: '', component: CredentialDefOverviewComponent },
  	{ path: 'selection', component: CredentialDefSelectionComponent },
  	{ path: 'connect', component: EstablishConnectionComponent },
  	{ path: 'verify', component: VerifyCredentialComponent },
  	{ path: 'result', component: ResultPageComponent },
	{ path: 'revoked', component: RevokedComponent },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
