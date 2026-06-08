import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MsalService } from '@azure/msal-angular';
import { AccountInfo } from '@azure/msal-browser';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  imports: [CommonModule],
  standalone: true,
})
export class ProfileComponent implements OnInit {
  account: AccountInfo | null = null;

  constructor(private readonly authService: MsalService) {}

  ngOnInit(): void {
    this.account = this.authService.instance.getActiveAccount() ?? this.authService.instance.getAllAccounts()[0] ?? null;
  }
}
