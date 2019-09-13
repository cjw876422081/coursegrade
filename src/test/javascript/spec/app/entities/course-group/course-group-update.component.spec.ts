/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseGroupUpdateComponent } from 'app/entities/course-group/course-group-update.component';
import { CourseGroupService } from 'app/entities/course-group/course-group.service';
import { CourseGroup } from 'app/shared/model/course-group.model';

describe('Component Tests', () => {
  describe('CourseGroup Management Update Component', () => {
    let comp: CourseGroupUpdateComponent;
    let fixture: ComponentFixture<CourseGroupUpdateComponent>;
    let service: CourseGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseGroupUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CourseGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseGroup(123);
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
        const entity = new CourseGroup();
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
