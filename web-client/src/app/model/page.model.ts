import { PageElement } from './page-element.model'

export class Page {

  constructor(public page: number,
              public pageSize: number,
              public totalPages: number,
              public totalElements: number,
              public elements: PageElement[]) { }

}
