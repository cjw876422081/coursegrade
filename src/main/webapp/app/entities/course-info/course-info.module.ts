import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CourseInfoComponent,
  CourseInfoDetailComponent,
  CourseInfoUpdateComponent,
  CourseInfoDeletePopupComponent,
  CourseInfoDeleteDialogComponent,
  courseInfoRoute,
  courseInfoPopupRoute
} from './';

const ENTITY_STATES = [...courseInfoRoute, ...courseInfoPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CourseInfoComponent,
    CourseInfoDetailComponent,
    CourseInfoUpdateComponent,
    CourseInfoDeleteDialogComponent,
    CourseInfoDeletePopupComponent
  ],
  entryComponents: [CourseInfoComponent, CourseInfoUpdateComponent, CourseInfoDeleteDialogComponent, CourseInfoDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCourseInfoModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
