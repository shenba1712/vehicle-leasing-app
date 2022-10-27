import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {LOCALE_ID} from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MatToolbarModule } from "@angular/material/toolbar";
import { AppRoutingModule } from "./app-routing.module";
import {MatButtonModule} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DashboardComponent } from './dashboard/dashboard.component';
import {HttpClientModule} from "@angular/common/http";
import { registerLocaleData } from '@angular/common';
import localeDE from '@angular/common/locales/de';

registerLocaleData(localeDE);

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FontAwesomeModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'de-DE' },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
