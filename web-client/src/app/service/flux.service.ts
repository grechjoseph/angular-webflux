import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class FluxService {

  eventSource: any = window['EventSource'];

  constructor() { }

  public getEventSourceObservable(): Observable<any> {
    console.log("Starting getAllPages.");
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux';
      let eventSource = new this.eventSource(url);

      eventSource.onmessage = event => {
          let json = JSON.parse(event.data);
          observer.next(json);
      };
      eventSource.onerror = (error) => {
          if(eventSource.readyState === 0) {
            console.log('The stream has been closed by the server.');
            eventSource.close();
            observer.complete();
          } else {
            observer.error('EventSource error: ' + error);
          }
      }
    });
  }

}
