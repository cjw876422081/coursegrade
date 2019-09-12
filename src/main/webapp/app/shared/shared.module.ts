import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CoursegradeSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [CoursegradeSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [CoursegradeSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeSharedModule {
  static forRoot() {
    return {
      ngModule: CoursegradeSharedModule
    };
  }
}
