import {Component, OnInit} from '@angular/core';
import {DefaultService} from 'build/generated/api-verifier/api/default.service';
import {SharedDataService} from "../service/shared-data.service";

@Component({
  selector: 'app-result-page',
  templateUrl: './result-page.component.html',
  styleUrls: ['./result-page.component.scss']
})
export class ResultPageComponent implements OnInit {

	verifiedClaims: Array<{ key: string; value: string; }> = [];

	constructor(private verifierService: DefaultService, private sharedDataService: SharedDataService) {
	}

	ngOnInit(): void {
		this.verifierService.getVerifiedClaims(this.sharedDataService.processId).subscribe(data => {
			for (let i = 0; i < Object.keys(data).length; i++) {
				this.verifiedClaims.push({ key: Object.keys(data)[i], value: Object.values(data)[i] })
			}
		});
	}
}
