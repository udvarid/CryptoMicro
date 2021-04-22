import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { AuthService } from 'src/app/auth/auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-history',
  templateUrl: './user-history.component.html',
  styleUrls: ['./user-history.component.css']
})
export class UserHistoryComponent implements OnInit {

  
  favoritePeriod: string = '15p';
  periods: string[] = ['15p', '1h', '4h', '1d', '1w'];
  periodTranslate: number[] = [1, 4, 16, 96, 96 * 7];

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getWalletHistory(this.authService.getActiveUser().userId, 96, this.periodLength()); 
  }

  periodLength(): number {
    return this.periodTranslate[this.periods.indexOf(this.favoritePeriod)];
  }

  onChange(mrChange: MatRadioChange) {
    this.userService.getWalletHistory(this.authService.getActiveUser().userId, 96, this.periodLength());
 } 

}
