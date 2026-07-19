import { Injectable, signal } from '@angular/core';
import { AccountInfo, PublicClientApplication } from '@azure/msal-browser';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly instance = new PublicClientApplication({
    auth: {
      clientId: environment.msalConfig.auth.clientId,
      authority: environment.msalConfig.auth.authority,
      redirectUri: environment.msalConfig.auth.redirectUri,
      postLogoutRedirectUri: environment.msalConfig.auth.postLogoutRedirectUri,
    },
    cache: {
      cacheLocation: 'sessionStorage',
    },
  });

  readonly ready = signal(false);
  readonly account = signal<AccountInfo | null>(null);

  async initialize(): Promise<void> {
    await this.instance.initialize();

    const result = await this.instance.handleRedirectPromise();
    const account = result?.account ?? this.instance.getAllAccounts()[0] ?? null;

    if (account) {
      this.instance.setActiveAccount(account);
    }

    this.account.set(account);
    this.ready.set(true);
  }

  async login(): Promise<void> {
    const result = await this.instance.loginPopup({
      scopes: environment.loginRequest.scopes,
      prompt: 'select_account',
    });

    this.instance.setActiveAccount(result.account);
    this.account.set(result.account);
  }

  async logout(): Promise<void> {
    const current = this.account();

    await this.instance.logoutPopup({
      account: current ?? undefined,
      postLogoutRedirectUri: environment.msalConfig.auth.postLogoutRedirectUri,
    });

    this.account.set(null);
  }
}
