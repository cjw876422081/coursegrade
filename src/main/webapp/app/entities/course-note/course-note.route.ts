import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseNote } from 'app/shared/model/course-note.model';
import { CourseNoteService } from './course-note.service';
import { CourseNoteComponent } from './course-note.component';
import { CourseNoteDetailComponent } from './course-note-detail.component';
import { CourseNoteUpdateComponent } from './course-note-update.component';
import { CourseNoteDeletePopupComponent } from './course-note-delete-dialog.component';
import { ICourseNote } from 'app/shared/model/course-note.model';

@Injectable({ providedIn: 'root' })
export class CourseNoteResolve implements Resolve<ICourseNote> {
  constructor(private service: CourseNoteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICourseNote> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CourseNote>) => response.ok),
        map((courseNote: HttpResponse<CourseNote>) => courseNote.body)
      );
    }
    return of(new CourseNote());
  }
}

export const courseNoteRoute: Routes = [
  {
    path: '',
    component: CourseNoteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.courseNote.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseNoteDetailComponent,
    resolve: {
      courseNote: CourseNoteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseNote.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseNoteUpdateComponent,
    resolve: {
      courseNote: CourseNoteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseNote.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseNoteUpdateComponent,
    resolve: {
      courseNote: CourseNoteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseNote.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const courseNotePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CourseNoteDeletePopupComponent,
    resolve: {
      courseNote: CourseNoteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseNote.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
