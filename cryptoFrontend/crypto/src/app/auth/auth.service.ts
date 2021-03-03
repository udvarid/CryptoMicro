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

    header_for_post = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      observe: "response" as 'body'
    };

    
    authenticated = new Subject<boolean>();
    userName = new Subject<UserDto>();
    pre: string;

    constructor(private http: HttpClient, private router: Router, private cookieService: CookieService) {
        this.pre = environment.apiUrl_user;
      }

    async logout() {
        const header = new HttpHeaders({});        
        await this.http.get(this.pre + '/user/logout', {headers: header}).toPromise();
        this.authenticated.next(false);
        const emptyUser: UserDto = {
          name: null,
          userId: null
        };
        this.userName.next(emptyUser);       
        this.cookieService.set('sessionId', null);    
        this.cookieService.set('name', null);    
        this.cookieService.set('userid', null);    
        this.router.navigate(['/auth']);
    }

    public login(userLogin: UserLoginDto) {                           
        this.http.post(this.pre + '/user/login', userLogin,  this.header_for_post)
          .pipe(tap(resp => resp['headers'].get('ReturnStatus')))          
          .subscribe((response) => {            
            this.cookieService.set('sessionId', response['headers']['headers'].get('sessionid'));    
            this.cookieService.set('name', response['headers']['headers'].get('name'));    
            this.cookieService.set('userid', response['headers']['headers'].get('userid'));    
            this.authenticated.next(true);
            const loggedInUser: UserDto = {
              name: response['headers']['headers'].get('name'),
              userId: response['headers']['headers'].get('userid')
            };
            this.userName.next(loggedInUser);
            this.router.navigate(['/user']);  
          });
    };
    

    public register(userRegister: RegisterDto) {   
        const header = new HttpHeaders({});        
        this.http.post(this.pre + '/user/register', userRegister, this.header_for_post)
          .pipe(tap(resp => resp['headers'].get('ReturnStatus')))
          .subscribe((response) => {            
            this.cookieService.set('sessionId', response['headers']['headers'].get('sessionid'));    
            this.cookieService.set('name', response['headers']['headers'].get('name'));    
            this.cookieService.set('userid', response['headers']['headers'].get('userid'));    
            this.authenticated.next(true);
            const registeredUser: UserDto = {
              name: response['headers']['headers'].get('name'),
              userId: response['headers']['headers'].get('userid')
            };
            this.userName.next(registeredUser);
            this.router.navigate(['/user']);  
          });

    };    

}
