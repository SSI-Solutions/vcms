import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {SharedDataService} from "../service/shared-data.service";

@Component({
  selector: 'app-credential-def-selection',
  templateUrl: './credential-def-selection.component.html',
  styleUrls: ['./credential-def-selection.component.scss']
})
export class CredentialDefSelectionComponent implements OnInit {
	formGroup = new FormGroup({});
	claims: string[];
	allComplete: boolean = true;

	constructor(private readonly router: Router, private sharedDataService: SharedDataService, private fb: FormBuilder) {
		this.claims = this.sharedDataService.credentialDefinition!.claims!;
	}

	ngOnInit(): void {
		this.getCredentialDefinitionClaims();
		this.setAll(true)
	}

	onSubmit(): void {
		this.sharedDataService.selectedClaims = [];
		for (const field in this.formGroup.controls) {
			const control = this.formGroup.get(field);
			if (control?.value === true) {
				this.sharedDataService.selectedClaims.push(field);
			}
		}
		console.log(this.sharedDataService.selectedClaims)
		this.router.navigate(['connect']).then();
	}

	isNoneChecked(): boolean {
		for (const field in this.formGroup.controls) {
			const control = this.formGroup.get(field);
			if (control?.value === true) {
				return false;
			}
		}
		return true;
	}

	setAll(completed: boolean) {
		this.allComplete = completed;
		for (const field in this.formGroup.controls) {
			this.formGroup.get(field)?.setValue(completed);
		}
	}

	private getCredentialDefinitionClaims() {
		this.claims.forEach(claim => {
			this.formGroup.addControl(claim, this.fb.control(false))
		});
	}
}
