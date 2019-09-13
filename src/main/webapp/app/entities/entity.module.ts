import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course-info',
        loadChildren: () => import('./course-info/course-info.module').then(m => m.CoursegradeCourseInfoModule)
      },
      {
        path: 'course-plan',
        loadChildren: () => import('./course-plan/course-plan.module').then(m => m.CoursegradeCoursePlanModule)
      },
      {
        path: 'course-homework',
        loadChildren: () => import('./course-homework/course-homework.module').then(m => m.CoursegradeCourseHomeworkModule)
      },
      {
        path: 'student-homework',
        loadChildren: () => import('./student-homework/student-homework.module').then(m => m.CoursegradeStudentHomeworkModule)
      },
      {
        path: 'course-note',
        loadChildren: () => import('./course-note/course-note.module').then(m => m.CoursegradeCourseNoteModule)
      },
      {
        path: 'student-course-group',
        loadChildren: () => import('./student-course-group/student-course-group.module').then(m => m.CoursegradeStudentCourseGroupModule)
      },
      {
        path: 'course-group',
        loadChildren: () => import('./course-group/course-group.module').then(m => m.CoursegradeCourseGroupModule)
      },
      {
        path: 'course-attachment',
        loadChildren: () => import('./course-attachment/course-attachment.module').then(m => m.CoursegradeCourseAttachmentModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoursegradeEntityModule {}
