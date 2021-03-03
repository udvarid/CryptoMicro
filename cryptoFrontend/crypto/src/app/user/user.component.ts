import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { UserDto } from '../shared/dto/user.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  actualUser: UserDto;

  actualUserChanged: Subscription;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.actualUserChanged = this.authService.userName.subscribe(user => this.actualUser = user);
  }

  ngOnDestroy(): void {
    this.actualUserChanged.unsubscribe();
  }


}
