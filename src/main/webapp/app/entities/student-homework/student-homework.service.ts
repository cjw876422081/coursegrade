import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStudentHomework } from 'app/shared/model/student-homework.model';

type EntityResponseType = HttpResponse<IStudentHomework>;
type EntityArrayResponseType = HttpResponse<IStudentHomework[]>;

@Injectable({ providedIn: 'root' })
export class StudentHomeworkService {
  public resourceUrl = SERVER_API_URL + 'api/student-homeworks';

  constructor(protected http: HttpClient) {}

  create(studentHomework: IStudentHomework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentHomework);
    return this.http
      .post<IStudentHomework>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentHomework: IStudentHomework): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentHomework);
    return this.http
      .put<IStudentHomework>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentHomework>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentHomework[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(studentHomework: IStudentHomework): IStudentHomework {
    const copy: IStudentHomework = Object.assign({}, studentHomework, {
      submitTime: studentHomework.submitTime != null && studentHomework.submitTime.isValid() ? studentHomework.submitTime.toJSON() : null,
      readTime: studentHomework.readTime != null && studentHomework.readTime.isValid() ? studentHomework.readTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.submitTime = res.body.submitTime != null ? moment(res.body.submitTime) : null;
      res.body.readTime = res.body.readTime != null ? moment(res.body.readTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studentHomework: IStudentHomework) => {
        studentHomework.submitTime = studentHomework.submitTime != null ? moment(studentHomework.submitTime) : null;
        studentHomework.readTime = studentHomework.readTime != null ? moment(studentHomework.readTime) : null;
      });
    }
    return res;
  }
}
