import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseAttachment } from 'app/shared/model/course-attachment.model';
import { CourseAttachmentService } from './course-attachment.service';
import { CourseAttachmentComponent } from './course-attachment.component';
import { CourseAttachmentDetailComponent } from './course-attachment-detail.component';
import { CourseAttachmentUpdateComponent } from './course-attachment-update.component';
import { CourseAttachmentDeletePopupComponent } from './course-attachment-delete-dialog.component';
import { ICourseAttachment } from 'app/shared/model/course-attachment.model';

@Injectable({ providedIn: 'root' })
export class CourseAttachmentResolve implements Resolve<ICourseAttachment> {
  constructor(private service: CourseAttachmentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICourseAttachment> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CourseAttachment>) => response.ok),
        map((courseAttachment: HttpResponse<CourseAttachment>) => courseAttachment.body)
      );
    }
    return of(new CourseAttachment());
  }
}

export const courseAttachmentRoute: Routes = [
  {
    path: '',
    component: CourseAttachmentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'coursegradeApp.courseAttachment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseAttachmentDetailComponent,
    resolve: {
      courseAttachment: CourseAttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseAttachment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseAttachmentUpdateComponent,
    resolve: {
      courseAttachment: CourseAttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseAttachment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseAttachmentUpdateComponent,
    resolve: {
      courseAttachment: CourseAttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseAttachment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const courseAttachmentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CourseAttachmentDeletePopupComponent,
    resolve: {
      courseAttachment: CourseAttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'coursegradeApp.courseAttachment.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
