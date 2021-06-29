import { Component, OnInit } from '@angular/core';
import { FluxService } from '../service/flux.service';
import { Observable } from 'rxjs';
import { Page } from '../model/page.model';
import { PageElement } from '../model/page-element.model';
import { PostRequest } from '../model/post-request.model';

@Component({
  selector: 'app-flux',
  templateUrl: './flux.component.html',
  styleUrls: ['./flux.component.css']
})
export class FluxComponent implements OnInit {

  currentSubscription;
  elements: PageElement[];
  currentPage: number = 1;
  totalPages: number = 100;
  hideLoadingBar = false;

  constructor(private fluxService: FluxService) { }

  ngOnInit() {
    this.getUsingEventSource();
  }

  // Subscribed to an Observable derived from the handling of EventSource.
  getUsingEventSource(): void {
    this.handleObservable(this.fluxService.getEventSourceObservable());
  }

  // Subscribed to an Observable derived from the handling of SSE. Ideal to use other Http Methods in addition to GET.
  getUsingSse(): void {
    this.handleObservable(this.fluxService.getSseObservable());
  }

  private handleObservable(observable: Observable<Page>): void {
    console.log("Getting pages.");
    if (this.currentSubscription) {
    console.log("Unsubscribing from previous subscription.");
      this.currentSubscription.unsubscribe();
    }

    console.log("Resetting values.");
    this.elements = [];
    this.currentPage = 1;
    this.totalPages = 100;
    this.hideLoadingBar = false;

    console.log("Subscribing...");
    this.currentSubscription = observable.subscribe(page => {
          console.log(page);

          this.currentPage = page.page;
          this.totalPages = page.totalPages;

          // Add each element from the page to the global list of elements.
          page.elements.forEach(element => {
            this.elements.push(element);
          });

          // If last page, set hideLoadingBar to true with a delay, so that the progress bar shows as 100% for the given delay.
          if (this.currentPage == this.totalPages) {
            setTimeout(() => {
              this.hideLoadingBar = true;
            }, 500);
          }
        });
  }

}
