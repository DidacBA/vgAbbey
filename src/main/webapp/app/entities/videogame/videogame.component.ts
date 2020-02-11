import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVideogame } from 'app/shared/model/videogame.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VideogameService } from './videogame.service';
import { VideogameDeleteDialogComponent } from './videogame-delete-dialog.component';

@Component({
  selector: 'jhi-videogame',
  templateUrl: './videogame.component.html'
})
export class VideogameComponent implements OnInit, OnDestroy {
  videogames: IVideogame[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected videogameService: VideogameService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.videogames = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.videogameService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IVideogame[]>) => this.paginateVideogames(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.videogames = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVideogames();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVideogame): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVideogames(): void {
    this.eventSubscriber = this.eventManager.subscribe('videogameListModification', () => this.reset());
  }

  delete(videogame: IVideogame): void {
    const modalRef = this.modalService.open(VideogameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.videogame = videogame;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVideogames(data: IVideogame[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.videogames.push(data[i]);
      }
    }
  }
}
