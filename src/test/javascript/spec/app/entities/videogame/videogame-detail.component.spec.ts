import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VgAbbeyTestModule } from '../../../test.module';
import { VideogameDetailComponent } from 'app/entities/videogame/videogame-detail.component';
import { Videogame } from 'app/shared/model/videogame.model';

describe('Component Tests', () => {
  describe('Videogame Management Detail Component', () => {
    let comp: VideogameDetailComponent;
    let fixture: ComponentFixture<VideogameDetailComponent>;
    const route = ({ data: of({ videogame: new Videogame(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VgAbbeyTestModule],
        declarations: [VideogameDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VideogameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VideogameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load videogame on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.videogame).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
