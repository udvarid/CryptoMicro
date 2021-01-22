import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Candle } from '../shared/dto/candle.model';
import { ReporterService } from './reporter.service';

@Component({
  selector: 'app-reporter',
  templateUrl: './reporter.component.html',
  styleUrls: ['./reporter.component.css']
})
export class ReporterComponent implements OnInit {  

  favoritePeriod = '15p';
  periods: string[] = ['15p', '1h', '4h', '1d', '1w'];
  periodTranslate: number[] = [1, 4, 16, 96, 96 * 7];

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {
    this.reporterService.getRecentData();
  }
  
  getCryptoHistoryData(cryptoPair: string) {
    this.reporterService.getCryptoHistoryData(cryptoPair, 96, this.periodTranslate[this.periods.indexOf(this.favoritePeriod)]);
  }

}
