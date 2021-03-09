import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class SessionInterceptor implements HttpInterceptor {
  constructor(public cookieService: CookieService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    request = request.clone({
      setHeaders: {
        sessionid: this.cookieService.get('sessionid')
      }
    });
    return next.handle(request);
  }
}