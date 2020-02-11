import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVideogame } from 'app/shared/model/videogame.model';

type EntityResponseType = HttpResponse<IVideogame>;
type EntityArrayResponseType = HttpResponse<IVideogame[]>;

@Injectable({ providedIn: 'root' })
export class VideogameService {
  public resourceUrl = SERVER_API_URL + 'api/videogames';

  constructor(protected http: HttpClient) {}

  create(videogame: IVideogame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(videogame);
    return this.http
      .post<IVideogame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(videogame: IVideogame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(videogame);
    return this.http
      .put<IVideogame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVideogame>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVideogame[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(videogame: IVideogame): IVideogame {
    const copy: IVideogame = Object.assign({}, videogame, {
      releaseDate: videogame.releaseDate && videogame.releaseDate.isValid() ? videogame.releaseDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.releaseDate = res.body.releaseDate ? moment(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((videogame: IVideogame) => {
        videogame.releaseDate = videogame.releaseDate ? moment(videogame.releaseDate) : undefined;
      });
    }
    return res;
  }
}
