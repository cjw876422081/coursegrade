import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  StudentCourseGroupComponent,
  StudentCourseGroupDetailComponent,
  StudentCourseGroupUpdateComponent,
  StudentCourseGroupDeletePopupComponent,
  StudentCourseGroupDeleteDialogComponent,
  studentCourseGroupRoute,
  studentCourseGroupPopupRoute
} from './';

const ENTITY_STATES = [...studentCourseGroupRoute, ...studentCourseGroupPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    StudentCourseGroupComponent,
    StudentCourseGroupDetailComponent,
    StudentCourseGroupUpdateComponent,
    StudentCourseGroupDeleteDialogComponent,
    StudentCourseGroupDeletePopupComponent
  ],
  entryComponents: [
    StudentCourseGroupComponent,
    StudentCourseGroupUpdateComponent,
    StudentCourseGroupDeleteDialogComponent,
    StudentCourseGroupDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeStudentCourseGroupModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
