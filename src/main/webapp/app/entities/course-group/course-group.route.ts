import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseGroup } from 'app/shared/model/course-group.model';
import { CourseGroupService } from './course-group.service';
import { CourseGroupComponent } from './course-group.component';
import { CourseGroupDetailComponent } from './course-group-detail.component';
import { CourseGroupUpdateComponent } from './course-group-update.component';
import { CourseGroupDeletePopupComponent } from './course-group-delete-dialog.component';
import { ICourseGroup } from 'app/shared/model/course-group.model';

@Injectable({ providedIn: 'root' })
export class CourseGroupResolve implements Resolve<ICourseGroup> {
  constructor(private service: CourseGroupService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICourseGroup> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CourseGroup>) => response.ok),
        map((courseGroup: HttpResponse<CourseGroup>) => courseGroup.body)
      );
    }
    return of(new CourseGroup());
  }
}

export const courseGroupRoute: Routes = [
  {
    path: '',
    component: CourseGroupComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.courseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseGroupDetailComponent,
    resolve: {
      courseGroup: CourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseGroupUpdateComponent,
    resolve: {
      courseGroup: CourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseGroupUpdateComponent,
    resolve: {
      courseGroup: CourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const courseGroupPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CourseGroupDeletePopupComponent,
    resolve: {
      courseGroup: CourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseGroup.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
