import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { VgAbbeyTestModule } from '../../../test.module';
import { VideogameUpdateComponent } from 'app/entities/videogame/videogame-update.component';
import { VideogameService } from 'app/entities/videogame/videogame.service';
import { Videogame } from 'app/shared/model/videogame.model';

describe('Component Tests', () => {
  describe('Videogame Management Update Component', () => {
    let comp: VideogameUpdateComponent;
    let fixture: ComponentFixture<VideogameUpdateComponent>;
    let service: VideogameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VgAbbeyTestModule],
        declarations: [VideogameUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VideogameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VideogameUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VideogameService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Videogame(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Videogame();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
