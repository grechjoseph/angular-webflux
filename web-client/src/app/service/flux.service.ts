import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { SSE } from 'sse.js';
import { Page } from '../model/page.model';
import { PostRequest } from '../model/post-request.model';

@Injectable()
export class FluxService {

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

  // A replacement for EventSource in order to support different Http Methods and a Request Body.
  // Requires npm install sse.js.
  public getSseObservable() : Observable<Page> {
    console.log("Starting getSseObservable.");
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux';
      let request = new PostRequest(3, 3);
      console.log("request: " + JSON.stringify(request));

      let source = new SSE(url,
        {headers: {
          'Content-Type': 'application/json',
          'other-header-goes-here': 'with-other-value-here'
        },
       payload: JSON.stringify(request),
       method: 'POST'});

      source.onmessage = (event) => {
        observer.next(JSON.parse(event.data));
      }

      source.stream();
    });
  }

}
