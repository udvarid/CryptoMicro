import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterialsModule } from './shared/materials.module';

import { AppComponent } from './app.component';
import { InfoComponent } from './info/info.component';
import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './header/header.component';
import { ReporterComponent } from './reporter/reporter.component';

@NgModule({
  declarations: [
    AppComponent,
    InfoComponent,
    HeaderComponent,
    ReporterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
