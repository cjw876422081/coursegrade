import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CourseHomeworkComponent,
  CourseHomeworkDetailComponent,
  CourseHomeworkUpdateComponent,
  CourseHomeworkDeletePopupComponent,
  CourseHomeworkDeleteDialogComponent,
  courseHomeworkRoute,
  courseHomeworkPopupRoute
} from './';

const ENTITY_STATES = [...courseHomeworkRoute, ...courseHomeworkPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CourseHomeworkComponent,
    CourseHomeworkDetailComponent,
    CourseHomeworkUpdateComponent,
    CourseHomeworkDeleteDialogComponent,
    CourseHomeworkDeletePopupComponent
  ],
  entryComponents: [
    CourseHomeworkComponent,
    CourseHomeworkUpdateComponent,
    CourseHomeworkDeleteDialogComponent,
    CourseHomeworkDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCourseHomeworkModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
