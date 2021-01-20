import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InfoComponent } from './info/info.component';
import { ReporterComponent } from './reporter/reporter.component';

const routes: Routes = [
  { path: '', redirectTo: '/info', pathMatch: 'full' },
  { path: 'info', component: InfoComponent },
  { path: 'report', component: ReporterComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }