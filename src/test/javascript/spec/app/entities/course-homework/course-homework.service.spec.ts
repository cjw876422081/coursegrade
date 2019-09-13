/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CourseHomeworkService } from 'app/entities/course-homework/course-homework.service';
import { ICourseHomework, CourseHomework } from 'app/shared/model/course-homework.model';

describe('Service Tests', () => {
  describe('CourseHomework Service', () => {
    let injector: TestBed;
    let service: CourseHomeworkService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseHomework;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CourseHomeworkService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CourseHomework(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            homeworkDeadline: currentDate.format(DATE_TIME_FORMAT),
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

      it('should create a CourseHomework', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            homeworkDeadline: currentDate.format(DATE_TIME_FORMAT),
            dataTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            homeworkDeadline: currentDate,
            dataTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new CourseHomework(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a CourseHomework', async () => {
        const returnedFromService = Object.assign(
          {
            homeworkCode: 'BBBBBB',
            homeworkMemo: 'BBBBBB',
            homeworkTarget: 'BBBBBB',
            homeworkGrade: 1,
            homeworkDeadline: currentDate.format(DATE_TIME_FORMAT),
            dataTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            homeworkDeadline: currentDate,
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

      it('should return a list of CourseHomework', async () => {
        const returnedFromService = Object.assign(
          {
            homeworkCode: 'BBBBBB',
            homeworkMemo: 'BBBBBB',
            homeworkTarget: 'BBBBBB',
            homeworkGrade: 1,
            homeworkDeadline: currentDate.format(DATE_TIME_FORMAT),
            dataTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            homeworkDeadline: currentDate,
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

      it('should delete a CourseHomework', async () => {
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
