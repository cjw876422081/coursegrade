/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseHomeworkDetailComponent } from 'app/entities/course-homework/course-homework-detail.component';
import { CourseHomework } from 'app/shared/model/course-homework.model';

describe('Component Tests', () => {
  describe('CourseHomework Management Detail Component', () => {
    let comp: CourseHomeworkDetailComponent;
    let fixture: ComponentFixture<CourseHomeworkDetailComponent>;
    const route = ({ data: of({ courseHomework: new CourseHomework(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseHomeworkDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CourseHomeworkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseHomeworkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseHomework).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
