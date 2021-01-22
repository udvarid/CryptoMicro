import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { Candle } from "../shared/dto/candle.model";

@Injectable({ providedIn: 'root' })

export class ReporterService {

    private pre: string;
    recentCandles = new Subject<Candle[]>();

    constructor(private http: HttpClient, private router: Router) {
        this.pre = environment.apiUrl;
    }    

    getRecentData() {        
        const header = new HttpHeaders({});
        this.http.get(this.pre + '/api/candle/recent', {headers: header,  withCredentials: true })
        .subscribe((response: Candle[]) => {
            this.recentCandles.next(response);                 
        });
    }

    //http hívás a candle lista felé, eredményt eltárolni és egy subjectbe is


}