import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FluxService } from '../service/flux.service';

@Component({
  selector: 'app-flux',
  templateUrl: './flux.component.html',
  styleUrls: ['./flux.component.css']
})
export class FluxComponent implements OnInit {

  elements: any[] = [];
  percentage: number = 0;

  constructor(private fluxService: FluxService, private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    console.log("From component.");
    this.fluxService.getAllPages().subscribe(page => {
      this.percentage = (100 * page.page) / page.totalPages;
      page.elements.forEach(element => {
        this.elements.push(element);
      });
      console.log(this.elements);
      this.changeDetector.detectChanges();
    });
  }

}
