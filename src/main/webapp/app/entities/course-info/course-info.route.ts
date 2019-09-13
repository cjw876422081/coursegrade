import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from './course-info.service';
import { CourseInfoComponent } from './course-info.component';
import { CourseInfoDetailComponent } from './course-info-detail.component';
import { CourseInfoUpdateComponent } from './course-info-update.component';
import { CourseInfoDeletePopupComponent } from './course-info-delete-dialog.component';
import { ICourseInfo } from 'app/shared/model/course-info.model';

@Injectable({ providedIn: 'root' })
export class CourseInfoResolve implements Resolve<ICourseInfo> {
  constructor(private service: CourseInfoService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICourseInfo> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CourseInfo>) => response.ok),
        map((courseInfo: HttpResponse<CourseInfo>) => courseInfo.body)
      );
    }
    return of(new CourseInfo());
  }
}

export const courseInfoRoute: Routes = [
  {
    path: '',
    component: CourseInfoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.courseInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseInfoDetailComponent,
    resolve: {
      courseInfo: CourseInfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseInfoUpdateComponent,
    resolve: {
      courseInfo: CourseInfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseInfoUpdateComponent,
    resolve: {
      courseInfo: CourseInfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const courseInfoPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CourseInfoDeletePopupComponent,
    resolve: {
      courseInfo: CourseInfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseInfo.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
