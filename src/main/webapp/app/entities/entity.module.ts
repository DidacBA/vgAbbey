import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'videogame',
        loadChildren: () => import('./videogame/videogame.module').then(m => m.VgAbbeyVideogameModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class VgAbbeyEntityModule {}
