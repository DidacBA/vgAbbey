import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VgAbbeySharedModule } from 'app/shared/shared.module';
import { VideogameComponent } from './videogame.component';
import { VideogameDetailComponent } from './videogame-detail.component';
import { VideogameUpdateComponent } from './videogame-update.component';
import { VideogameDeleteDialogComponent } from './videogame-delete-dialog.component';
import { videogameRoute } from './videogame.route';

@NgModule({
  imports: [VgAbbeySharedModule, RouterModule.forChild(videogameRoute)],
  declarations: [VideogameComponent, VideogameDetailComponent, VideogameUpdateComponent, VideogameDeleteDialogComponent],
  entryComponents: [VideogameDeleteDialogComponent]
})
export class VgAbbeyVideogameModule {}
