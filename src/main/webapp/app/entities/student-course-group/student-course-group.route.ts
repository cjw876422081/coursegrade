import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StudentCourseGroup } from 'app/shared/model/student-course-group.model';
import { StudentCourseGroupService } from './student-course-group.service';
import { StudentCourseGroupComponent } from './student-course-group.component';
import { StudentCourseGroupDetailComponent } from './student-course-group-detail.component';
import { StudentCourseGroupUpdateComponent } from './student-course-group-update.component';
import { StudentCourseGroupDeletePopupComponent } from './student-course-group-delete-dialog.component';
import { IStudentCourseGroup } from 'app/shared/model/student-course-group.model';

@Injectable({ providedIn: 'root' })
export class StudentCourseGroupResolve implements Resolve<IStudentCourseGroup> {
  constructor(private service: StudentCourseGroupService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudentCourseGroup> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudentCourseGroup>) => response.ok),
        map((studentCourseGroup: HttpResponse<StudentCourseGroup>) => studentCourseGroup.body)
      );
    }
    return of(new StudentCourseGroup());
  }
}

export const studentCourseGroupRoute: Routes = [
  {
    path: '',
    component: StudentCourseGroupComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.studentCourseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentCourseGroupDetailComponent,
    resolve: {
      studentCourseGroup: StudentCourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentCourseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentCourseGroupUpdateComponent,
    resolve: {
      studentCourseGroup: StudentCourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentCourseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentCourseGroupUpdateComponent,
    resolve: {
      studentCourseGroup: StudentCourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentCourseGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studentCourseGroupPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudentCourseGroupDeletePopupComponent,
    resolve: {
      studentCourseGroup: StudentCourseGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentCourseGroup.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
