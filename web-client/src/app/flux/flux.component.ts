import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FluxService } from '../service/flux.service';
import { Element } from '../model/element.model';

@Component({
  selector: 'app-flux',
  templateUrl: './flux.component.html',
  styleUrls: ['./flux.component.css']
})
export class FluxComponent implements OnInit {

  elements: any[] = [];

  constructor(private fluxService: FluxService, private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    console.log("From component.");
    this.fluxService.getAllPages().subscribe(newElements => {
      newElements.forEach(element => {
        this.elements.push(element);
      });
      console.log(this.elements);
      this.changeDetector.detectChanges();
    });
  }

}
