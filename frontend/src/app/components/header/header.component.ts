import { Component, OnInit, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  user = this.authService.currentUser();
  isLoggedIn = false;
  menuOpen = false;

  constructor(private authService: AuthService, private router: Router) {
    effect(() => {
      this.user = this.authService.currentUser();
      this.isLoggedIn = !!this.user;
    });
  }

  ngOnInit(): void {
    this.user = this.authService.currentUser();
    this.isLoggedIn = !!this.user;
  }

  isAdmin(): boolean {
    return this.user?.role === 'ADMIN';
  }

  isManager(): boolean {
    return this.user?.role === 'MANAGER';
  }

  isVendor(): boolean {
    return this.user?.role === 'VENDOR';
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.closeMenu();
  }

  navigate(path: string): void {
    this.router.navigate([path]);
    this.closeMenu();
  }
}
