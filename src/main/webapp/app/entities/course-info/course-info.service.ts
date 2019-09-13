import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseInfo } from 'app/shared/model/course-info.model';

type EntityResponseType = HttpResponse<ICourseInfo>;
type EntityArrayResponseType = HttpResponse<ICourseInfo[]>;

@Injectable({ providedIn: 'root' })
export class CourseInfoService {
  public resourceUrl = SERVER_API_URL + 'api/course-infos';

  constructor(protected http: HttpClient) {}

  create(courseInfo: ICourseInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseInfo);
    return this.http
      .post<ICourseInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseInfo: ICourseInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseInfo);
    return this.http
      .put<ICourseInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseInfo: ICourseInfo): ICourseInfo {
    const copy: ICourseInfo = Object.assign({}, courseInfo, {
      dataTime: courseInfo.dataTime != null && courseInfo.dataTime.isValid() ? courseInfo.dataTime.toJSON() : null
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
      res.body.forEach((courseInfo: ICourseInfo) => {
        courseInfo.dataTime = courseInfo.dataTime != null ? moment(courseInfo.dataTime) : null;
      });
    }
    return res;
  }
}
