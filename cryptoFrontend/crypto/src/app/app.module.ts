import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterialsModule } from './shared/materials.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { InfoComponent } from './info/info.component';
import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './header/header.component';
import { ReporterComponent } from './reporter/reporter.component';
import { RecentCandleComponent } from './shared/recent-candle/recent-candle.component';
import { HistoryCandleComponent } from './shared/history-candle/history-candle.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthComponent } from './auth/auth.component';
import { UserComponent } from './user/user.component';
import { SessionInterceptor } from './shared/interceptors/http-session.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    InfoComponent,
    HeaderComponent,
    ReporterComponent,
    RecentCandleComponent,
    HistoryCandleComponent,
    AuthComponent,
    UserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxChartsModule,
    BrowserAnimationsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
