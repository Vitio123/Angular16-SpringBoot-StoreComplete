import { Component } from '@angular/core';
import { UserStorageService } from './services/storage/user-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'EcommerceWeb';

  isCustomerLoggedIn: boolean = UserStorageService.isCustomerLoggenIn();
  isAdminLoggedIn: boolean = UserStorageService.isAdminLoggenIn();

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      this.isCustomerLoggedIn = UserStorageService.isCustomerLoggenIn();
      this.isAdminLoggedIn = UserStorageService.isAdminLoggenIn();
    });
  }

  logout() {
    UserStorageService.signOut();
    this.router.navigateByUrl('login');
  }
}
