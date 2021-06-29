import { PageElement } from './page-element.model'

export class Page {

  page: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  elements: PageElement[];

  constructor() { }

}
