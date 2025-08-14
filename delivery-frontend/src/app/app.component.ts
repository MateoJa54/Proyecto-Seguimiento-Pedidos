// src/app/app.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from './core/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent implements OnInit {
  currentYear = new Date().getFullYear();

  constructor(public auth: AuthService) {}

  ngOnInit(): void {
    // inicializa el cliente OAuth (carga discovery y check de login)
    this.auth.init().then(()=> {
  console.log('AccessToken:', this.auth.getAccessToken());
  console.log('IdentityClaims:', this.auth.getIdentityClaims());
});

  }

  logout(): void {
    this.auth.logout();
  }
}
