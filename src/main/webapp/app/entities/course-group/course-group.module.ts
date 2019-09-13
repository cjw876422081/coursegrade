import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CourseGroupComponent,
  CourseGroupDetailComponent,
  CourseGroupUpdateComponent,
  CourseGroupDeletePopupComponent,
  CourseGroupDeleteDialogComponent,
  courseGroupRoute,
  courseGroupPopupRoute
} from './';

const ENTITY_STATES = [...courseGroupRoute, ...courseGroupPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CourseGroupComponent,
    CourseGroupDetailComponent,
    CourseGroupUpdateComponent,
    CourseGroupDeleteDialogComponent,
    CourseGroupDeletePopupComponent
  ],
  entryComponents: [CourseGroupComponent, CourseGroupUpdateComponent, CourseGroupDeleteDialogComponent, CourseGroupDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCourseGroupModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
