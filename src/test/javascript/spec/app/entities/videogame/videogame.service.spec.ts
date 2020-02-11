import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { VideogameService } from 'app/entities/videogame/videogame.service';
import { IVideogame, Videogame } from 'app/shared/model/videogame.model';

describe('Service Tests', () => {
  describe('Videogame Service', () => {
    let injector: TestBed;
    let service: VideogameService;
    let httpMock: HttpTestingController;
    let elemDefault: IVideogame;
    let expectedResult: IVideogame | IVideogame[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(VideogameService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Videogame(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            releaseDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Videogame', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            releaseDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.create(new Videogame()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Videogame', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            imageUrl: 'BBBBBB',
            repositoryUrl: 'BBBBBB',
            deployUrl: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            isSearchingCollab: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Videogame', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            imageUrl: 'BBBBBB',
            repositoryUrl: 'BBBBBB',
            deployUrl: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            isSearchingCollab: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Videogame', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
