import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SSE } from 'sse.js';

import { Page } from '../models/page.model';
import { PostRequest } from '../models/post-request.model';

@Injectable()
export class SseService {

  constructor() { }

  public streamUpdatableString() : Observable<string> {
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux/updatable';
      let httpMethod = 'GET';

      let request = {
        method: httpMethod
      };

      let source = new SSE(url, request);

      source.onmessage = (event) => {
        observer.next(event.data);
      }

      source.stream();
    });
  }

  public streamServerDateTime() : Observable<string> {
    return new Observable((observer) => {
      let url = 'http://localhost:8080/server-datetime';
      let httpMethod = 'GET';

      let request = {
        method: httpMethod
      };

      let source = new SSE(url, request);

      source.onmessage = (event) => {
        observer.next(event.data);
      }

      source.stream();
    });
  }

  // A replacement for EventSource in order to support different Http Methods and a Request Body.
  // Requires npm install sse.js.
  public getObservable(path: string) : Observable<Page> {
    return new Observable((observer) => {
      let url = 'http://localhost:8080/flux' + path;
      let requestBody = new PostRequest(3, 3);
      let requestHeaders = {
        'Content-Type': 'application/json',
        'other-headdder-goes-here': 'with-other-value-here'
      };
      let httpMethod = 'POST';

      let request = {
        headers: requestHeaders,
        payload: JSON.stringify(requestBody),
        method: httpMethod
      };

      let source = new SSE(url, request);

      source.onmessage = (event) => {
        observer.next(JSON.parse(event.data));
      }

      source.stream();
    });
  }
}
