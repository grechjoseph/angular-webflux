import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { FluxComponent } from './flux/flux.component';

import { EventSourceService } from './service/event-source.service';
import { SseService } from './service/sse.service';


@NgModule({
  declarations: [
    AppComponent,
    FluxComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [
    EventSourceService,
    SseService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
