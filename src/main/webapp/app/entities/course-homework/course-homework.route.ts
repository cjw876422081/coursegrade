import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseHomework } from 'app/shared/model/course-homework.model';
import { CourseHomeworkService } from './course-homework.service';
import { CourseHomeworkComponent } from './course-homework.component';
import { CourseHomeworkDetailComponent } from './course-homework-detail.component';
import { CourseHomeworkUpdateComponent } from './course-homework-update.component';
import { CourseHomeworkDeletePopupComponent } from './course-homework-delete-dialog.component';
import { ICourseHomework } from 'app/shared/model/course-homework.model';

@Injectable({ providedIn: 'root' })
export class CourseHomeworkResolve implements Resolve<ICourseHomework> {
  constructor(private service: CourseHomeworkService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICourseHomework> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CourseHomework>) => response.ok),
        map((courseHomework: HttpResponse<CourseHomework>) => courseHomework.body)
      );
    }
    return of(new CourseHomework());
  }
}

export const courseHomeworkRoute: Routes = [
  {
    path: '',
    component: CourseHomeworkComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.courseHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseHomeworkDetailComponent,
    resolve: {
      courseHomework: CourseHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseHomeworkUpdateComponent,
    resolve: {
      courseHomework: CourseHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseHomeworkUpdateComponent,
    resolve: {
      courseHomework: CourseHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const courseHomeworkPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CourseHomeworkDeletePopupComponent,
    resolve: {
      courseHomework: CourseHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseHomework.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
