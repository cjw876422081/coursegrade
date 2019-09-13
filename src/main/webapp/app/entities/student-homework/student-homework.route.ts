import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StudentHomework } from 'app/shared/model/student-homework.model';
import { StudentHomeworkService } from './student-homework.service';
import { StudentHomeworkComponent } from './student-homework.component';
import { StudentHomeworkDetailComponent } from './student-homework-detail.component';
import { StudentHomeworkUpdateComponent } from './student-homework-update.component';
import { StudentHomeworkDeletePopupComponent } from './student-homework-delete-dialog.component';
import { IStudentHomework } from 'app/shared/model/student-homework.model';

@Injectable({ providedIn: 'root' })
export class StudentHomeworkResolve implements Resolve<IStudentHomework> {
  constructor(private service: StudentHomeworkService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudentHomework> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<StudentHomework>) => response.ok),
        map((studentHomework: HttpResponse<StudentHomework>) => studentHomework.body)
      );
    }
    return of(new StudentHomework());
  }
}

export const studentHomeworkRoute: Routes = [
  {
    path: '',
    component: StudentHomeworkComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.studentHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentHomeworkDetailComponent,
    resolve: {
      studentHomework: StudentHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentHomeworkUpdateComponent,
    resolve: {
      studentHomework: StudentHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentHomeworkUpdateComponent,
    resolve: {
      studentHomework: StudentHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentHomework.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const studentHomeworkPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StudentHomeworkDeletePopupComponent,
    resolve: {
      studentHomework: StudentHomeworkResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.studentHomework.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
