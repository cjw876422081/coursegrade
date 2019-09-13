import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseAttachment } from 'app/shared/model/course-attachment.model';

type EntityResponseType = HttpResponse<ICourseAttachment>;
type EntityArrayResponseType = HttpResponse<ICourseAttachment[]>;

@Injectable({ providedIn: 'root' })
export class CourseAttachmentService {
  public resourceUrl = SERVER_API_URL + 'api/course-attachments';

  constructor(protected http: HttpClient) {}

  create(courseAttachment: ICourseAttachment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseAttachment);
    return this.http
      .post<ICourseAttachment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseAttachment: ICourseAttachment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseAttachment);
    return this.http
      .put<ICourseAttachment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseAttachment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseAttachment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseAttachment: ICourseAttachment): ICourseAttachment {
    const copy: ICourseAttachment = Object.assign({}, courseAttachment, {
      uploadTime: courseAttachment.uploadTime != null && courseAttachment.uploadTime.isValid() ? courseAttachment.uploadTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.uploadTime = res.body.uploadTime != null ? moment(res.body.uploadTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseAttachment: ICourseAttachment) => {
        courseAttachment.uploadTime = courseAttachment.uploadTime != null ? moment(courseAttachment.uploadTime) : null;
      });
    }
    return res;
  }
}
