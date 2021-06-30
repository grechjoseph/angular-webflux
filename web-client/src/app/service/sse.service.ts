import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SSE } from 'sse.js';

import { Page } from '../model/page.model';
import { PostRequest } from '../model/post-request.model';

@Injectable()
export class SseService {

  constructor() { }

  // A replacement for EventSource in order to support different Http Methods and a Request Body.
  // Requires npm install sse.js.
  public getObservable() : Observable<Page> {
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
