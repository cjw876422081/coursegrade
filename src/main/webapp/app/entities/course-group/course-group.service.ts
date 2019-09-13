import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseGroup } from 'app/shared/model/course-group.model';

type EntityResponseType = HttpResponse<ICourseGroup>;
type EntityArrayResponseType = HttpResponse<ICourseGroup[]>;

@Injectable({ providedIn: 'root' })
export class CourseGroupService {
  public resourceUrl = SERVER_API_URL + 'api/course-groups';

  constructor(protected http: HttpClient) {}

  create(courseGroup: ICourseGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseGroup);
    return this.http
      .post<ICourseGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseGroup: ICourseGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseGroup);
    return this.http
      .put<ICourseGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseGroup: ICourseGroup): ICourseGroup {
    const copy: ICourseGroup = Object.assign({}, courseGroup, {
      dataTime: courseGroup.dataTime != null && courseGroup.dataTime.isValid() ? courseGroup.dataTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataTime = res.body.dataTime != null ? moment(res.body.dataTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseGroup: ICourseGroup) => {
        courseGroup.dataTime = courseGroup.dataTime != null ? moment(courseGroup.dataTime) : null;
      });
    }
    return res;
  }
}
