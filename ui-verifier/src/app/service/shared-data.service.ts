import {Injectable} from '@angular/core';
import {CredentialDefinition} from 'build/generated/api-verifier/model/models';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {

	connectionId: string = '';
	processId: string = '';
	credentialDefinition?: CredentialDefinition;
	selectedClaims: Array<string> = [];
}
