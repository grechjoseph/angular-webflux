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
  loading = true;
  currentPage: number = 1;
  totalPages: number = 100;
  percentage: number = 0;

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
    this.loading = true;
    this.currentPage = 1;
    this.totalPages = 100;
    this.percentage = 0;

    console.log("Subscribing...");
    this.currentSubscription = this.fluxService.getAllPages().subscribe(page => {
          console.log(page);
          this.currentPage = page.page;
          this.totalPages = page.totalPages;
          this.percentage = (100 * this.currentPage) / this.totalPages;
          page.elements.forEach(element => {
            this.elements.push(element);
          });
          this.changeDetector.detectChanges();

          if (this.currentPage == this.totalPages) {
            setTimeout(() => {
              this.loading = false;
              this.changeDetector.detectChanges();
            }, 500);
          }
        });
  }

}
