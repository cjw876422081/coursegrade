import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CoursePlan } from 'app/shared/model/course-plan.model';
import { CoursePlanService } from './course-plan.service';
import { CoursePlanComponent } from './course-plan.component';
import { CoursePlanDetailComponent } from './course-plan-detail.component';
import { CoursePlanUpdateComponent } from './course-plan-update.component';
import { CoursePlanDeletePopupComponent } from './course-plan-delete-dialog.component';
import { ICoursePlan } from 'app/shared/model/course-plan.model';

@Injectable({ providedIn: 'root' })
export class CoursePlanResolve implements Resolve<ICoursePlan> {
  constructor(private service: CoursePlanService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICoursePlan> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CoursePlan>) => response.ok),
        map((coursePlan: HttpResponse<CoursePlan>) => coursePlan.body)
      );
    }
    return of(new CoursePlan());
  }
}

export const coursePlanRoute: Routes = [
  {
    path: '',
    component: CoursePlanComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.coursePlan.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CoursePlanDetailComponent,
    resolve: {
      coursePlan: CoursePlanResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.coursePlan.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CoursePlanUpdateComponent,
    resolve: {
      coursePlan: CoursePlanResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.coursePlan.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CoursePlanUpdateComponent,
    resolve: {
      coursePlan: CoursePlanResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.coursePlan.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const coursePlanPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CoursePlanDeletePopupComponent,
    resolve: {
      coursePlan: CoursePlanResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.coursePlan.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
