import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseNote } from 'app/shared/model/course-note.model';

type EntityResponseType = HttpResponse<ICourseNote>;
type EntityArrayResponseType = HttpResponse<ICourseNote[]>;

@Injectable({ providedIn: 'root' })
export class CourseNoteService {
  public resourceUrl = SERVER_API_URL + 'api/course-notes';

  constructor(protected http: HttpClient) {}

  create(courseNote: ICourseNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseNote);
    return this.http
      .post<ICourseNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseNote: ICourseNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseNote);
    return this.http
      .put<ICourseNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseNote: ICourseNote): ICourseNote {
    const copy: ICourseNote = Object.assign({}, courseNote, {
      noteTime: courseNote.noteTime != null && courseNote.noteTime.isValid() ? courseNote.noteTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.noteTime = res.body.noteTime != null ? moment(res.body.noteTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseNote: ICourseNote) => {
        courseNote.noteTime = courseNote.noteTime != null ? moment(courseNote.noteTime) : null;
      });
    }
    return res;
  }
}
