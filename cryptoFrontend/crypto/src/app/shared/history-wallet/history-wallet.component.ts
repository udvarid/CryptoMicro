import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { UserService } from 'src/app/user/user.service';
import { WalletHistoryDto } from '../dto/user.model';

@Component({
  selector: 'app-history-wallet',
  templateUrl: './history-wallet.component.html',
  styleUrls: ['./history-wallet.component.css']
})
export class HistoryWalletComponent implements OnInit, OnDestroy {

  multi: any[] 

  chartStep: number;
  chartStepMultiplier: number;

  view: any[]

  // options
  showXAxis: boolean = true;
  showYAxis: boolean = true;
  gradient: boolean = false;
  showLegend: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Time';
  showYAxisLabel: boolean = true;
  yAxisLabel: string = 'Value';
  animations: boolean = true;
  barPadding: number = 1;
  legendPosition: string = 'below'

  colorScheme = {
    domain: ['#5AA454', '#C7B42C', '#AAAAAA']
  };

  public userWalletHistory: WalletHistoryDto[];
  userWalletHistoryChanged: Subscription;  

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {

    this.chartStep = 100;
    this.chartStepMultiplier = window.innerWidth > 1600 ? 3 : 2;
    this.view = [this.chartStep * this.chartStepMultiplier * 2.75, this.chartStep * this.chartStepMultiplier * 1.75];

    this.userWalletHistoryChanged = this.userService.walletHistory.subscribe(walletHistory => {
            this.userWalletHistory = walletHistory;      
            console.log(this.userWalletHistory);
            this.draw(this.userWalletHistory);
          }      
      );
    this.userService.getWalletHistory(this.authService.getActiveUser().userId);
  }

  ngOnDestroy(): void {
    this.userWalletHistoryChanged.unsubscribe();
  }

  onSelect(event) {
    console.log(event);
  }

  draw(walletHistory: WalletHistoryDto[]) {
    this.multi = [];
    walletHistory.forEach(wallet => {
      const indexOfT = wallet.time.indexOf('T') + 1;        
      const onePoint = {        
        name: wallet.time.substr(5, indexOfT + 5).replace('T', ' '),
        series: []
      }
      onePoint.series.push(this.getSeriesPoint(wallet, 'USD'));
      onePoint.series.push(this.getSeriesPoint(wallet, 'BTC'));
      onePoint.series.push(this.getSeriesPoint(wallet, 'ETH'));
      this.multi.push(onePoint);           
    });    
  }

  getSeriesPoint(wallet: WalletHistoryDto, ccy: string): any {
    const seriesPoint = {
      name : ccy,
      value : wallet.detailedAmount.find(w => w.ccy === ccy).amount
    }
    return seriesPoint;
  }

}
