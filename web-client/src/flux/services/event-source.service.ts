import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

import { Page } from '../models/page.model';

@Injectable()
export class EventSourceService {

  eventSource: any = window['EventSource'];

  // NgZone ensures that changes are detected by the angular zone.
  constructor(private _zone: NgZone) { }

  public getEventSourceObservable(): Observable<Page> {
    console.log("Starting getEventSourceObservable.");
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux';
      let eventSource = new this.eventSource(url);

      eventSource.onmessage = (event) => {
          let page: Page = JSON.parse(event.data);
          this._zone.run(() => observer.next(page));
      };
      eventSource.onerror = (error) => {
          if(eventSource.readyState === 0) {
            console.log('The stream has been closed by the server.');
            eventSource.close();
            this._zone.run(() => observer.complete());
          } else {
            this._zone.run(() => observer.error('EventSource error: ' + error));
          }
      }
    });
  }

}
