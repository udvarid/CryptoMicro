import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Candle } from '../shared/dto/candle.model';
import { ReporterService } from './reporter.service';

@Component({
  selector: 'app-reporter',
  templateUrl: './reporter.component.html',
  styleUrls: ['./reporter.component.css']
})
export class ReporterComponent implements OnInit, OnDestroy {  

  private favoritCryptoPair: string = '';
  private timer: any;
  private recentCandlesSubscription: Subscription = new Subscription();
  private recentCandles: Candle[];
  private oldRecentCandles: Candle[];


  favoritePeriod: string = '15p';
  periods: string[] = ['15p', '1h', '4h', '1d', '1w'];
  periodTranslate: number[] = [1, 4, 16, 96, 96 * 7];

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {    
    this.recentCandlesSubscription = this.reporterService.recentCandles.subscribe( 
      (response: Candle[]) => {this.recentCandles = response;})        

    const checkIn = () => {
      this.reporterService.getRecentData();
      if (this.favoritCryptoPair.length > 0 && this.refreshed()) {
          this.getCryptoHistoryData(this.favoritCryptoPair);
          this.oldRecentCandles = this.recentCandles;
      }      
    }
    checkIn();
    this.timer = setInterval(checkIn, 60000);

  }

  refreshed(): boolean {
    return !this.oldRecentCandles || this.recentCandles[0]['time'] !== this.oldRecentCandles[0]['time'];
  }
  
  getCryptoHistoryData(cryptoPair: string) {
    this.favoritCryptoPair = cryptoPair;
    this.reporterService.getCryptoHistoryData(cryptoPair, 96, this.periodTranslate[this.periods.indexOf(this.favoritePeriod)]);
  }

  ngOnDestroy(): void {
    clearInterval(this.timer);
    this.recentCandlesSubscription.unsubscribe();
  }

}
