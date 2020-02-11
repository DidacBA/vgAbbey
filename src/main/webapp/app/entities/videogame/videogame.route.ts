import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVideogame, Videogame } from 'app/shared/model/videogame.model';
import { VideogameService } from './videogame.service';
import { VideogameComponent } from './videogame.component';
import { VideogameDetailComponent } from './videogame-detail.component';
import { VideogameUpdateComponent } from './videogame-update.component';

@Injectable({ providedIn: 'root' })
export class VideogameResolve implements Resolve<IVideogame> {
  constructor(private service: VideogameService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVideogame> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((videogame: HttpResponse<Videogame>) => {
          if (videogame.body) {
            return of(videogame.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Videogame());
  }
}

export const videogameRoute: Routes = [
  {
    path: '',
    component: VideogameComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'vgAbbeyApp.videogame.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VideogameDetailComponent,
    resolve: {
      videogame: VideogameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'vgAbbeyApp.videogame.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VideogameUpdateComponent,
    resolve: {
      videogame: VideogameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'vgAbbeyApp.videogame.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VideogameUpdateComponent,
    resolve: {
      videogame: VideogameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'vgAbbeyApp.videogame.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
