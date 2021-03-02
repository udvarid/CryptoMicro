import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isAuthenticated = false;

  authChangedSubs: Subscription;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authChangedSubs = this.authService.authenticated.subscribe(auth => this.isAuthenticated = auth);
  }

  onLogout() {
    this.authService.logout();    
  }

  
  ngOnDestroy() {
    this.authChangedSubs.unsubscribe();    
  }

}
