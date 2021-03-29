import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { WalletHistoryDto } from 'src/app/shared/dto/user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-history',
  templateUrl: './user-history.component.html',
  styleUrls: ['./user-history.component.css']
})
export class UserHistoryComponent implements OnInit, OnDestroy {

  public userWalletHistory: WalletHistoryDto[];
  userWalletHistoryChanged: Subscription;  

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.userWalletHistoryChanged = this.userService.walletHistory.subscribe(walletHistory => {
            this.userWalletHistory = walletHistory;      
            console.log(this.userWalletHistory);
          }      
      );
    this.userService.getWalletHistory(this.authService.getActiveUser().userId);
  }

  ngOnDestroy(): void {
    this.userWalletHistoryChanged.unsubscribe();
  }

}
