/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CoursePlanUpdateComponent } from 'app/entities/course-plan/course-plan-update.component';
import { CoursePlanService } from 'app/entities/course-plan/course-plan.service';
import { CoursePlan } from 'app/shared/model/course-plan.model';

describe('Component Tests', () => {
  describe('CoursePlan Management Update Component', () => {
    let comp: CoursePlanUpdateComponent;
    let fixture: ComponentFixture<CoursePlanUpdateComponent>;
    let service: CoursePlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CoursePlanUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CoursePlanUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CoursePlanUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CoursePlanService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CoursePlan(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CoursePlan();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
