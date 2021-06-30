import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';

import { FluxModule } from '../flux/flux.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FluxModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
