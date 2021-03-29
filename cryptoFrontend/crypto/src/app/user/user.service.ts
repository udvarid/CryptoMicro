import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { UserDto, WalletHistoryDto } from "../shared/dto/user.model";


@Injectable({ providedIn: 'root' })
export class UserService {

    pre: string;

    userInfo = new Subject<UserDto>();
    walletHistory = new Subject<WalletHistoryDto[]>();

    constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) {
        this.pre = environment.apiUrl_user;
    }

    public getWallet(userId: string) {    
        const header = new HttpHeaders({userId: userId});
        this.http.get(this.pre + '/api/user/userinfo', {headers: header})          
          .subscribe((response: UserDto) => {                        
              this.userInfo.next(response);
          });
    };

    public getWalletHistory(userId: string) {    
        const header = new HttpHeaders({userId: userId});
        this.http.get(this.pre + '/api/user/walletHistory', {headers: header})          
          .subscribe((response: WalletHistoryDto[]) => {                        
              this.walletHistory.next(response);
          });
    };

}