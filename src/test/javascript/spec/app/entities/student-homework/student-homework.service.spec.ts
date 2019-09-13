/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { StudentHomeworkService } from 'app/entities/student-homework/student-homework.service';
import { IStudentHomework, StudentHomework } from 'app/shared/model/student-homework.model';

describe('Service Tests', () => {
  describe('StudentHomework Service', () => {
    let injector: TestBed;
    let service: StudentHomeworkService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentHomework;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(StudentHomeworkService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new StudentHomework(0, 'AAAAAAA', currentDate, currentDate, 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            submitTime: currentDate.format(DATE_TIME_FORMAT),
            readTime: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a StudentHomework', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            submitTime: currentDate.format(DATE_TIME_FORMAT),
            readTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            submitTime: currentDate,
            readTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new StudentHomework(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a StudentHomework', async () => {
        const returnedFromService = Object.assign(
          {
            submitMemo: 'BBBBBB',
            submitTime: currentDate.format(DATE_TIME_FORMAT),
            readTime: currentDate.format(DATE_TIME_FORMAT),
            grade: 1,
            student: 'BBBBBB',
            teacher: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            submitTime: currentDate,
            readTime: currentDate
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

      it('should return a list of StudentHomework', async () => {
        const returnedFromService = Object.assign(
          {
            submitMemo: 'BBBBBB',
            submitTime: currentDate.format(DATE_TIME_FORMAT),
            readTime: currentDate.format(DATE_TIME_FORMAT),
            grade: 1,
            student: 'BBBBBB',
            teacher: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            submitTime: currentDate,
            readTime: currentDate
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

      it('should delete a StudentHomework', async () => {
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
