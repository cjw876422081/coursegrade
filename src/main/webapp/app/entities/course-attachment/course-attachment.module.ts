import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CourseAttachmentComponent,
  CourseAttachmentDetailComponent,
  CourseAttachmentUpdateComponent,
  CourseAttachmentDeletePopupComponent,
  CourseAttachmentDeleteDialogComponent,
  courseAttachmentRoute,
  courseAttachmentPopupRoute
} from './';

const ENTITY_STATES = [...courseAttachmentRoute, ...courseAttachmentPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CourseAttachmentComponent,
    CourseAttachmentDetailComponent,
    CourseAttachmentUpdateComponent,
    CourseAttachmentDeleteDialogComponent,
    CourseAttachmentDeletePopupComponent
  ],
  entryComponents: [
    CourseAttachmentComponent,
    CourseAttachmentUpdateComponent,
    CourseAttachmentDeleteDialogComponent,
    CourseAttachmentDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCourseAttachmentModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
