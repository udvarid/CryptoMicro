import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ReporterService } from 'src/app/reporter/reporter.service';
import { Candle } from '../dto/candle.model';

@Component({
  selector: 'app-recent-candle',
  templateUrl: './recent-candle.component.html',
  styleUrls: ['./recent-candle.component.css']
})
export class RecentCandleComponent implements OnInit, OnDestroy {

  private recentCandlesSubscription: Subscription = new Subscription();
  recentCandles: Candle[];

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {
    this.recentCandlesSubscription = this.reporterService.recentCandles
    .subscribe( (response: Candle[]) => {
                this.recentCandles = response;
                }
              )    

  }

  ngOnDestroy(): void {
    this.recentCandlesSubscription.unsubscribe();
  }

  getActualPrice(pair: string): number {
    return this.recentCandles.find(candle => candle.currencyPair === pair).close;
  }


}
