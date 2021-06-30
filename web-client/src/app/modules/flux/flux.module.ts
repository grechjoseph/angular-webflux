import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FluxComponent } from './components/flux.component';

import { EventSourceService } from './services/event-source.service';
import { SseService } from './services/sse.service';

@NgModule({
  imports: [
    CommonModule
  ],
  exports: [
    FluxComponent
  ],
  declarations: [
    FluxComponent
  ],
  providers: [
    EventSourceService,
    SseService
  ]
})
export class FluxModule { }
