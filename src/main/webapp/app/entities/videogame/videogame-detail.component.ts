import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVideogame } from 'app/shared/model/videogame.model';

@Component({
  selector: 'jhi-videogame-detail',
  templateUrl: './videogame-detail.component.html'
})
export class VideogameDetailComponent implements OnInit {
  videogame: IVideogame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videogame }) => (this.videogame = videogame));
  }

  previousState(): void {
    window.history.back();
  }
}
