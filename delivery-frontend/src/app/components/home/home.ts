import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService } from '../../core/auth';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.html'
})
export class HomeComponent {
  orderIdInput = '';

  constructor(private router: Router, public auth: AuthService) {}

  go(idInput: string) {
    const id = Number(idInput);
    if (!id || isNaN(id)) return;
    this.router.navigate(['/orders', id]);
  }
}
