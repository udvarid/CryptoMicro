import { Component, OnInit } from '@angular/core';
import { Candle } from '../shared/dto/candle.model';
import { ReporterService } from './reporter.service';

@Component({
  selector: 'app-reporter',
  templateUrl: './reporter.component.html',
  styleUrls: ['./reporter.component.css']
})
export class ReporterComponent implements OnInit {

  recentCandles: Candle[];
  candleHistory: Candle[];

  favoritePeriod = '15p';
  periods: string[] = ['15p', '1h', '4h', '1d', '1w'];
  periodTranslate: number[] = [1, 4, 16, 96, 96 * 7];

  constructor(private reporterService: ReporterService) { }

  ngOnInit(): void {

    //subscibe-olni a service object-jeire
  }

  getRecentCryptoData() {
    //this.cryptoService.getBloombergData();
  }

  getCryptoHistoryData(cryptoPair: string) {
    //this.cryptoService.getCryptoHistoryData(cryptoPair, 96, this.periodTranslate[this.periods.indexOf(this.favoritePeriod)]);
  }

  //legyen unsubscibe is

}
