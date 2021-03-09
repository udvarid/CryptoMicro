import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { UserDto } from '../shared/dto/user.model';
import { UserService } from './user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  public userInfo: UserDto;
  userInfoChanged: Subscription;

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.userInfoChanged = this.userService.userInfo.subscribe(user => {
            this.userInfo = user;
            console.log(this.userInfo);
          }      
      );
    this.userService.getWallet(this.authService.getActiveUser().userId);
  }
  

  ngOnDestroy(): void {
    this.userInfoChanged.unsubscribe();
  }


}
