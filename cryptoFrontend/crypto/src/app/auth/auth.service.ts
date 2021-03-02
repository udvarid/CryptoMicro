import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { RegisterDto, UserDto, UserLoginDto } from "../shared/dto/user.model";
import { tap } from 'rxjs/operators';
import { CookieService } from "ngx-cookie-service";

@Injectable({ providedIn: 'root' })
export class AuthService {
    
    authenticated = new Subject<boolean>();
    userName = new Subject<UserDto>();
    pre: string;

    constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) {
        this.pre = environment.apiUrl;
      }

    async logout() {
        const header = new HttpHeaders({});        
        await this.http.get(this.pre + '/api/user/logout', {headers: header}).toPromise();
        this.authenticated.next(false);
        const emptyUser: UserDto = {
          name: null,
          userId: null
        };
        this.userName.next(emptyUser);        
        this.router.navigate(['/auth']);
    }

    public login(userLogin: UserLoginDto) {           
        console.log(this.pre);
        const header = new HttpHeaders({});        
        this.http.post(this.pre + '/api/user/login', userLogin, {headers: header})
          .pipe(tap(resp => resp['headers'].get('ReturnStatus')))
          .subscribe((resp) => {
            this.cookieService.set('sessionId', resp['headers']['headers'].get('sessionId')[0]);    
            this.authenticated.next(true);
          });
    };
    

    public register(userRegister: RegisterDto) {   
        const header = new HttpHeaders({});        
        this.http.post(this.pre + '/api/user/register', userRegister, {headers: header})
          .pipe(tap(resp => resp['headers'].get('ReturnStatus')))
          .subscribe((resp) => {
            this.cookieService.set('sessionId', resp['headers']['headers'].get('sessionId')[0]);    
            this.authenticated.next(true);
          });
    };    

}
