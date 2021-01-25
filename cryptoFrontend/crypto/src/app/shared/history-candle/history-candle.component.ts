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

  chartStep: number;
  chartStepMultiplier: number;

  view: any[];
  multi = [];

  legend = true;
  showLabels = true;
  animations = true;
  xAxis = true;
  yAxis = true;
  showYAxisLabel = true;
  showXAxisLabel = true;
  xAxisLabel = 'Time';
  yAxisLabel = 'Price';
  timeline = true;
  maxValueY = 0;
  minValueY = 0;

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {    
    this.chartStep = 100;
    this.chartStepMultiplier = window.innerWidth > 1600 ? 3 : 2;
    this.view = [this.chartStep * this.chartStepMultiplier * 5, this.chartStep * this.chartStepMultiplier * 2];

    this.historyCandlesSubscription = this.reporterService.historyCandles.subscribe( 
                    (response: Candle[]) => {
                      this.historyCandles = response;
                      this.draw(this.historyCandles);
                    }
              )    
  }

  draw(candles: Candle[]) {
    const candleTypes = [
      {name : 'High',
       column : 'high'},
       {name : 'Close',
       column : 'close'},
       {name : 'Low',
       column : 'low'}
    ];
    this.multi = [];

    candleTypes.forEach( ct => {
      const transformedData = {
        name: ct.name,
        series: []
      };
      candles.forEach( candle => {
        const indexOfT = candle.time.indexOf('T') + 1;        
        const onePoint = {          
          name: candle.time.substr(0, indexOfT + 5).replace('T', ' '),
          value: candle[ct.column]
        };
        transformedData.series.push(onePoint);
      });
      this.multi.push(transformedData);
    });

    this.calculateMinMax();
  }

  calculateMinMax() {
    let minCandle = 1000000000;
    let maxCandle = 0;
    this.historyCandles.forEach(candle => {
      if (candle.low < minCandle) {
        minCandle = candle.low;
      }
      if (candle.high > maxCandle) {
        maxCandle = candle.high;
      }
    });
    const band = 0.005;
    this.maxValueY = Math.ceil(maxCandle * (1 + band));
    this.minValueY = Math.ceil(minCandle * (1 - band)) - 1;
  }


  onSelect(data): void {
    //console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data): void {
    //console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    //console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  ngOnDestroy(): void {
    //this.historyCandlesSubscription.unsubscribe();
  }

}
