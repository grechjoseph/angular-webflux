import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { FluxComponent } from './flux/flux.component';

import { FluxService } from './service/flux.service';


@NgModule({
  declarations: [
    AppComponent,
    FluxComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [
    FluxService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
