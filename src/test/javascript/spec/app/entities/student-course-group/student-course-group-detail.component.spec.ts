/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { StudentCourseGroupDetailComponent } from 'app/entities/student-course-group/student-course-group-detail.component';
import { StudentCourseGroup } from 'app/shared/model/student-course-group.model';

describe('Component Tests', () => {
  describe('StudentCourseGroup Management Detail Component', () => {
    let comp: StudentCourseGroupDetailComponent;
    let fixture: ComponentFixture<StudentCourseGroupDetailComponent>;
    const route = ({ data: of({ studentCourseGroup: new StudentCourseGroup(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [StudentCourseGroupDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudentCourseGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentCourseGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentCourseGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
