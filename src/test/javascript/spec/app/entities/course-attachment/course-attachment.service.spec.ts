/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CourseAttachmentService } from 'app/entities/course-attachment/course-attachment.service';
import { ICourseAttachment, CourseAttachment } from 'app/shared/model/course-attachment.model';

describe('Service Tests', () => {
  describe('CourseAttachment Service', () => {
    let injector: TestBed;
    let service: CourseAttachmentService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourseAttachment;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CourseAttachmentService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CourseAttachment(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            uploadTime: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a CourseAttachment', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            uploadTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            uploadTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new CourseAttachment(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a CourseAttachment', async () => {
        const returnedFromService = Object.assign(
          {
            attachmentType: 'BBBBBB',
            attachmentUse: 'BBBBBB',
            fileName: 'BBBBBB',
            originName: 'BBBBBB',
            filePath: 'BBBBBB',
            fileSize: 1,
            uploadTime: currentDate.format(DATE_TIME_FORMAT),
            fileUser: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            uploadTime: currentDate
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

      it('should return a list of CourseAttachment', async () => {
        const returnedFromService = Object.assign(
          {
            attachmentType: 'BBBBBB',
            attachmentUse: 'BBBBBB',
            fileName: 'BBBBBB',
            originName: 'BBBBBB',
            filePath: 'BBBBBB',
            fileSize: 1,
            uploadTime: currentDate.format(DATE_TIME_FORMAT),
            fileUser: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            uploadTime: currentDate
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

      it('should delete a CourseAttachment', async () => {
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
