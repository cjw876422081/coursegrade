import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseHomework } from 'app/shared/model/course-homework.model';

type EntityResponseType = HttpResponse<ICourseHomework>;
type EntityArrayResponseType = HttpResponse<ICourseHomework[]>;

@Injectable({ providedIn: 'root' })
export class CourseHomeworkService {
  public resourceUrl = SERVER_API_URL + 'api/course-homeworks';

  constructor(protected http: HttpClient) {}

  create(courseHomework: ICourseHomework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseHomework);
    return this.http
      .post<ICourseHomework>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseHomework: ICourseHomework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseHomework);
    return this.http
      .put<ICourseHomework>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseHomework>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseHomework[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseHomework: ICourseHomework): ICourseHomework {
    const copy: ICourseHomework = Object.assign({}, courseHomework, {
      homeworkDeadline:
        courseHomework.homeworkDeadline != null && courseHomework.homeworkDeadline.isValid()
          ? courseHomework.homeworkDeadline.toJSON()
          : null,
      dataTime: courseHomework.dataTime != null && courseHomework.dataTime.isValid() ? courseHomework.dataTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.homeworkDeadline = res.body.homeworkDeadline != null ? moment(res.body.homeworkDeadline) : null;
      res.body.dataTime = res.body.dataTime != null ? moment(res.body.dataTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseHomework: ICourseHomework) => {
        courseHomework.homeworkDeadline = courseHomework.homeworkDeadline != null ? moment(courseHomework.homeworkDeadline) : null;
        courseHomework.dataTime = courseHomework.dataTime != null ? moment(courseHomework.dataTime) : null;
      });
    }
    return res;
  }
}
