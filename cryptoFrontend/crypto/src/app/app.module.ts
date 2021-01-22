import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterialsModule } from './shared/materials.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { InfoComponent } from './info/info.component';
import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './header/header.component';
import { ReporterComponent } from './reporter/reporter.component';
import { RecentCandleComponent } from './shared/recent-candle/recent-candle.component';

@NgModule({
  declarations: [
    AppComponent,
    InfoComponent,
    HeaderComponent,
    ReporterComponent,
    RecentCandleComponent 
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
