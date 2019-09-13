/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CoursePlanDetailComponent } from 'app/entities/course-plan/course-plan-detail.component';
import { CoursePlan } from 'app/shared/model/course-plan.model';

describe('Component Tests', () => {
  describe('CoursePlan Management Detail Component', () => {
    let comp: CoursePlanDetailComponent;
    let fixture: ComponentFixture<CoursePlanDetailComponent>;
    const route = ({ data: of({ coursePlan: new CoursePlan(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CoursePlanDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CoursePlanDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoursePlanDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.coursePlan).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
