import { OnDestroy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ReporterService } from 'src/app/reporter/reporter.service';
import { Candle } from '../dto/candle.model';

@Component({
  selector: 'app-history-candle',
  templateUrl: './history-candle.component.html',
  styleUrls: ['./history-candle.component.css']
})
export class HistoryCandleComponent implements OnInit, OnDestroy {

  private historyCandlesSubscription: Subscription = new Subscription();
  historyCandles: Candle[];

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {    
    this.historyCandlesSubscription = this.reporterService.recentCandles
    .subscribe( (response: Candle[]) => {
                this.historyCandles = response;
                }
              )    
  }

  ngOnDestroy(): void {
    this.historyCandlesSubscription.unsubscribe();
  }

}
