import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FluxService } from '../service/flux.service';

@Component({
  selector: 'app-flux',
  templateUrl: './flux.component.html',
  styleUrls: ['./flux.component.css']
})
export class FluxComponent implements OnInit {

  currentSubscription;
  elements: any[] = [];
  currentPage: number = 1;
  totalPages: number = 100;
  hideLoadingBar = false;

  constructor(private fluxService: FluxService, private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    this.getAllPages();
  }

  getAllPages(): void {
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
    // Choose between getEventSourceObservable or getSseObservable (both return Observable).
    // this.currentSubscription = this.fluxService.getEventSourceObservable().subscribe(page => {
    this.currentSubscription = this.fluxService.getSseObservable().subscribe(page => {
          console.log(page);

          this.currentPage = page.page;
          this.totalPages = page.totalPages;

          // Add each element from the page to the global list of elements.
          page.elements.forEach(element => {
            this.elements.push(element);
          });

          this.changeDetector.detectChanges();

          // If last page, set hideLoadingBar to true with a delay, so that the progress bar shows as 100% for the given delay.
          if (this.currentPage == this.totalPages) {
            setTimeout(() => {
              this.hideLoadingBar = true;
              this.changeDetector.detectChanges();
            }, 500);
          }
        });
  }

}
