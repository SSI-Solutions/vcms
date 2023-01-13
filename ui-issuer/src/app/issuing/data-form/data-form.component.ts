import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {SharedDataService} from "../service/shared-data.service";

@Component({
	selector: 'app-data-form',
	templateUrl: './data-form.component.html',
	styleUrls: ['./data-form.component.scss']
})
export class DataFormComponent implements OnInit {
	dataForm = new FormGroup({});
	claims: string[];

	constructor(private readonly router: Router, private sharedDataService: SharedDataService, private fb: FormBuilder) {
		this.claims = this.sharedDataService.credentialDefinition!.claims!;
	}

	ngOnInit(): void {
		this.getCredentialDefinitionClaims();
	}

	onSubmit(): void {
		if (this.dataForm.status === 'VALID') {
			this.sharedDataService.dataForm = this.dataForm;
			this.router.navigate(['connect']).then();
		}
	}

	private getCredentialDefinitionClaims() {
		this.claims.forEach(claim => {
			this.dataForm.addControl(claim, this.fb.control('', [Validators.required]));
		});
	}
}
