/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CourseInfoService } from 'app/entities/course-info/course-info.service';
import { ICourseInfo, CourseInfo } from 'app/shared/model/course-info.model';

describe('Service Tests', () => {
  describe('CourseInfo Service', () => {
    let injector: TestBed;
    let service: CourseInfoService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseInfo;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CourseInfoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CourseInfo(0, 'AAAAAAA', 'AAAAAAA', 0, 0, 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dataTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a CourseInfo', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new CourseInfo(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a CourseInfo', async () => {
        const returnedFromService = Object.assign(
          {
            courseCode: 'BBBBBB',
            courseName: 'BBBBBB',
            courseCount: 1,
            courseWeekCount: 1,
            courseMemo: 'BBBBBB',
            dataTime: currentDate.format(DATE_TIME_FORMAT),
            courseUser: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataTime: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of CourseInfo', async () => {
        const returnedFromService = Object.assign(
          {
            courseCode: 'BBBBBB',
            courseName: 'BBBBBB',
            courseCount: 1,
            courseWeekCount: 1,
            courseMemo: 'BBBBBB',
            dataTime: currentDate.format(DATE_TIME_FORMAT),
            courseUser: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataTime: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CourseInfo', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

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
