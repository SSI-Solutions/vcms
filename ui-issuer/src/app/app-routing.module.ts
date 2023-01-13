import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CredentialDefOverviewComponent } from "./issuing/credential-def-overview/credential-def-overview.component";
import { DataFormComponent } from "./issuing/data-form/data-form.component";
import { EstablishConnectionComponent } from "./issuing/establish-connection/establish-connection.component";
import { AcceptOfferComponent } from "./issuing/accept-offer/accept-offer.component";
import { ResultPageComponent } from "./issuing/result-page/result-page.component";
import { AdminUiHoldersComponent } from "./admin/admin-ui-holders/admin-ui-holders.component";
import { AdminUiConnectionsComponent } from "./admin/admin-ui-connections/admin-ui-connections.component";

const routes: Routes = [
  	{ path: '', component: CredentialDefOverviewComponent },
  	{ path: 'data', component: DataFormComponent },
  	{ path: 'connect', component: EstablishConnectionComponent },
  	{ path: 'offer', component: AcceptOfferComponent },
  	{ path: 'result', component: ResultPageComponent },
	{ path: 'admin', component: AdminUiHoldersComponent },
	{ path: 'admin/:id', component: AdminUiConnectionsComponent },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
