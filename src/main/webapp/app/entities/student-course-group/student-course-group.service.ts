import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStudentCourseGroup } from 'app/shared/model/student-course-group.model';

type EntityResponseType = HttpResponse<IStudentCourseGroup>;
type EntityArrayResponseType = HttpResponse<IStudentCourseGroup[]>;

@Injectable({ providedIn: 'root' })
export class StudentCourseGroupService {
  public resourceUrl = SERVER_API_URL + 'api/student-course-groups';

  constructor(protected http: HttpClient) {}

  create(studentCourseGroup: IStudentCourseGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentCourseGroup);
    return this.http
      .post<IStudentCourseGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(studentCourseGroup: IStudentCourseGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentCourseGroup);
    return this.http
      .put<IStudentCourseGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudentCourseGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudentCourseGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(studentCourseGroup: IStudentCourseGroup): IStudentCourseGroup {
    const copy: IStudentCourseGroup = Object.assign({}, studentCourseGroup, {
      joinTime: studentCourseGroup.joinTime != null && studentCourseGroup.joinTime.isValid() ? studentCourseGroup.joinTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.joinTime = res.body.joinTime != null ? moment(res.body.joinTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((studentCourseGroup: IStudentCourseGroup) => {
        studentCourseGroup.joinTime = studentCourseGroup.joinTime != null ? moment(studentCourseGroup.joinTime) : null;
      });
    }
    return res;
  }
}
