import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CourseNoteComponent,
  CourseNoteDetailComponent,
  CourseNoteUpdateComponent,
  CourseNoteDeletePopupComponent,
  CourseNoteDeleteDialogComponent,
  courseNoteRoute,
  courseNotePopupRoute
} from './';

const ENTITY_STATES = [...courseNoteRoute, ...courseNotePopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CourseNoteComponent,
    CourseNoteDetailComponent,
    CourseNoteUpdateComponent,
    CourseNoteDeleteDialogComponent,
    CourseNoteDeletePopupComponent
  ],
  entryComponents: [CourseNoteComponent, CourseNoteUpdateComponent, CourseNoteDeleteDialogComponent, CourseNoteDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCourseNoteModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
