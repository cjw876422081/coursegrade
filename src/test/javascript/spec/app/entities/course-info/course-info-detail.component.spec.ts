/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseInfoDetailComponent } from 'app/entities/course-info/course-info-detail.component';
import { CourseInfo } from 'app/shared/model/course-info.model';

describe('Component Tests', () => {
  describe('CourseInfo Management Detail Component', () => {
    let comp: CourseInfoDetailComponent;
    let fixture: ComponentFixture<CourseInfoDetailComponent>;
    const route = ({ data: of({ courseInfo: new CourseInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CourseInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
