import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { SSE } from 'sse.js';

@Injectable()
export class FluxService {

  eventSource: any = window['EventSource'];

  // NgZone ensures that changes are detected by the angular zone.
  constructor(private _zone: NgZone) { }

  public getEventSourceObservable(): Observable<any> {
    console.log("Starting getEventSourceObservable.");
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux';
      let eventSource = new this.eventSource(url);

      eventSource.onmessage = event => {
          let json = JSON.parse(event.data);
          this._zone.run(() => observer.next(json));
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
  public getSseObservable() : Observable<any> {
    console.log("Starting getSseObservable.");
        return new Observable((observer) => {
          let url = 'http://localhost:8080/flux';
          let source = new SSE(url,
            {headers: {
              'Content-Type': 'application/json',
              'other-header-goes-here': 'with-other-value-here'
            },
           payload: JSON.stringify({
              totalPages: 3,
              elementsPerPage: 3
           }),
           method: 'POST'});

          source.addEventListener('message', function(event) {
              let json = JSON.parse(event.data);
              observer.next(json);
              });

          source.stream();
        });
  }

}
