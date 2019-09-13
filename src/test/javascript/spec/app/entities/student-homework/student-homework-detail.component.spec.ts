/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { StudentHomeworkDetailComponent } from 'app/entities/student-homework/student-homework-detail.component';
import { StudentHomework } from 'app/shared/model/student-homework.model';

describe('Component Tests', () => {
  describe('StudentHomework Management Detail Component', () => {
    let comp: StudentHomeworkDetailComponent;
    let fixture: ComponentFixture<StudentHomeworkDetailComponent>;
    const route = ({ data: of({ studentHomework: new StudentHomework(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [StudentHomeworkDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudentHomeworkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentHomeworkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentHomework).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
