import {Injectable} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {CredentialDefinition} from 'build/generated/api-issuer/model/credentialDefinition';
import {HolderResponse} from 'build/generated/api-issuer/model/holderResponse';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {

	connectionId: string = "";
	processId: string = '';
	credentialDefinition?: CredentialDefinition;
	dataForm?: FormGroup;
	holder?: HolderResponse;
}
