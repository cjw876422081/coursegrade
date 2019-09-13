import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICoursePlan } from 'app/shared/model/course-plan.model';

type EntityResponseType = HttpResponse<ICoursePlan>;
type EntityArrayResponseType = HttpResponse<ICoursePlan[]>;

@Injectable({ providedIn: 'root' })
export class CoursePlanService {
  public resourceUrl = SERVER_API_URL + 'api/course-plans';

  constructor(protected http: HttpClient) {}

  create(coursePlan: ICoursePlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coursePlan);
    return this.http
      .post<ICoursePlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(coursePlan: ICoursePlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coursePlan);
    return this.http
      .put<ICoursePlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICoursePlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICoursePlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(coursePlan: ICoursePlan): ICoursePlan {
    const copy: ICoursePlan = Object.assign({}, coursePlan, {
      dataTime: coursePlan.dataTime != null && coursePlan.dataTime.isValid() ? coursePlan.dataTime.toJSON() : null
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
      res.body.forEach((coursePlan: ICoursePlan) => {
        coursePlan.dataTime = coursePlan.dataTime != null ? moment(coursePlan.dataTime) : null;
      });
    }
    return res;
  }
}
