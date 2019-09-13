import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CoursegradeSharedModule } from 'app/shared';
import {
  CoursePlanComponent,
  CoursePlanDetailComponent,
  CoursePlanUpdateComponent,
  CoursePlanDeletePopupComponent,
  CoursePlanDeleteDialogComponent,
  coursePlanRoute,
  coursePlanPopupRoute
} from './';

const ENTITY_STATES = [...coursePlanRoute, ...coursePlanPopupRoute];

@NgModule({
  imports: [CoursegradeSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CoursePlanComponent,
    CoursePlanDetailComponent,
    CoursePlanUpdateComponent,
    CoursePlanDeleteDialogComponent,
    CoursePlanDeletePopupComponent
  ],
  entryComponents: [CoursePlanComponent, CoursePlanUpdateComponent, CoursePlanDeleteDialogComponent, CoursePlanDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeCoursePlanModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
