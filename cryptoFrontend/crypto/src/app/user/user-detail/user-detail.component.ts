import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { UserDto, Wallet } from 'src/app/shared/dto/user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit, OnDestroy {

  public userInfo: UserDto;
  userInfoChanged: Subscription;  
  ccys: Wallet[];

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.userInfoChanged = this.userService.userInfo.subscribe(user => {
            this.userInfo = user;
            this.ccys = this.orderedWallet(user.wallets);            
          }      
      );
    this.userService.getWallet(this.authService.getActiveUser().userId);
  }

  orderedWallet(wallets: Wallet[]) : Wallet[] {
    return wallets.filter(w => w.ccy === 'USD').concat(wallets.filter(w => w.ccy !== 'USD'));
  }

  ngOnDestroy(): void {
    this.userInfoChanged.unsubscribe();
  }

}
