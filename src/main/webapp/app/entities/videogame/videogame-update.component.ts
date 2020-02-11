import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVideogame, Videogame } from 'app/shared/model/videogame.model';
import { VideogameService } from './videogame.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-videogame-update',
  templateUrl: './videogame-update.component.html'
})
export class VideogameUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(256)]],
    description: [null, [Validators.minLength(5), Validators.maxLength(256)]],
    imageUrl: [null, [Validators.maxLength(256)]],
    repositoryUrl: [null, [Validators.required, Validators.maxLength(256)]],
    deployUrl: [null, [Validators.maxLength(256)]],
    releaseDate: [],
    isSearchingCollab: [null, [Validators.required]],
    developers: []
  });

  constructor(
    protected videogameService: VideogameService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videogame }) => {
      if (!videogame.id) {
        const today = moment().startOf('day');
        videogame.releaseDate = today;
      }

      this.updateForm(videogame);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(videogame: IVideogame): void {
    this.editForm.patchValue({
      id: videogame.id,
      title: videogame.title,
      description: videogame.description,
      imageUrl: videogame.imageUrl,
      repositoryUrl: videogame.repositoryUrl,
      deployUrl: videogame.deployUrl,
      releaseDate: videogame.releaseDate ? videogame.releaseDate.format(DATE_TIME_FORMAT) : null,
      isSearchingCollab: videogame.isSearchingCollab,
      developers: videogame.developers
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const videogame = this.createFromForm();
    if (videogame.id !== undefined) {
      this.subscribeToSaveResponse(this.videogameService.update(videogame));
    } else {
      this.subscribeToSaveResponse(this.videogameService.create(videogame));
    }
  }

  private createFromForm(): IVideogame {
    return {
      ...new Videogame(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      repositoryUrl: this.editForm.get(['repositoryUrl'])!.value,
      deployUrl: this.editForm.get(['deployUrl'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? moment(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      isSearchingCollab: this.editForm.get(['isSearchingCollab'])!.value,
      developers: this.editForm.get(['developers'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideogame>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
