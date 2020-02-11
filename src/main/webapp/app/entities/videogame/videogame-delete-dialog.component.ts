import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVideogame } from 'app/shared/model/videogame.model';
import { VideogameService } from './videogame.service';

@Component({
  templateUrl: './videogame-delete-dialog.component.html'
})
export class VideogameDeleteDialogComponent {
  videogame?: IVideogame;

  constructor(protected videogameService: VideogameService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.videogameService.delete(id).subscribe(() => {
      this.eventManager.broadcast('videogameListModification');
      this.activeModal.close();
    });
  }
}
