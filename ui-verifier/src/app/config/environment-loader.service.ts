import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';

export interface EnvConfig {
	verifierBasePath: string;
	connectorBasePath: string;
}


@Injectable()
export class EnvironmentLoaderService {
	private envConfig!: EnvConfig;

	constructor(private readonly http: HttpClient) {}

	async loadEnvConfig(configPath: string): Promise<void> {
		this.envConfig = await lastValueFrom(this.http.get<EnvConfig>(configPath));
	}

	getEnvConfig(): EnvConfig {
		return this.envConfig;
	}
}
