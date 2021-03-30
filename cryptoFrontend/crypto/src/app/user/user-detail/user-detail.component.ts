import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { UserDto, Wallet, WalletHistoryDto } from 'src/app/shared/dto/user.model';
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

  public recentUserWallet: WalletHistoryDto;
  userWalletHistoryChanged: Subscription;  


  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.userInfoChanged = this.userService.userInfo.subscribe(user => {
            this.userInfo = user;
            this.ccys = this.orderedWallet(user.wallets);            
          }      
      );
    this.userWalletHistoryChanged = this.userService.walletHistory.subscribe((walletHistory: WalletHistoryDto[]) => {
        this.recentUserWallet = walletHistory[walletHistory.length - 1];  
        console.log(this.recentUserWallet)            ;
      }      
  );
    this.userService.getWallet(this.authService.getActiveUser().userId);
  }

  orderedWallet(wallets: Wallet[]) : Wallet[] {
    return wallets.filter(w => w.ccy === 'USD').concat(wallets.filter(w => w.ccy !== 'USD'));
  }

  ngOnDestroy(): void {
    this.userInfoChanged.unsubscribe();
    this.userWalletHistoryChanged.unsubscribe();
  }

  hasWalletInfo(ccy: String): boolean { 
    return this.recentUserWallet && this.recentUserWallet.detailedAmount.find(w => w.ccy === ccy).amount > 0;
  }

  getWalletInfo(ccy: String): number { 
    return this.recentUserWallet.detailedAmount.find(w => w.ccy === ccy).amount;
  }

}
