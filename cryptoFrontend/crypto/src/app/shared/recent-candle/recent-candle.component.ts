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
    this.recentCandlesSubscription = this.reporterService.recentCandles.subscribe( 
                    (response: Candle[]) => {this.recentCandles = response;}
              )    
  }

  getActualPrice(pair: string): number {
    const price = this.recentCandles.find(candle => candle.currencyPair === pair).close;
    const rounder = Math.pow(10, this.getRounder(price));
    return Math.round(price * rounder) / rounder;
  }

  private getRounder(price: number) : number {
    if (price > 1000) {
      return 1;
    } else if (price < 100) {
      return 3;
    }
    return 2;
  }

  ngOnDestroy(): void {
    this.recentCandlesSubscription.unsubscribe();
  }

}
