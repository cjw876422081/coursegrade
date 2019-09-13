/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { StudentCourseGroupUpdateComponent } from 'app/entities/student-course-group/student-course-group-update.component';
import { StudentCourseGroupService } from 'app/entities/student-course-group/student-course-group.service';
import { StudentCourseGroup } from 'app/shared/model/student-course-group.model';

describe('Component Tests', () => {
  describe('StudentCourseGroup Management Update Component', () => {
    let comp: StudentCourseGroupUpdateComponent;
    let fixture: ComponentFixture<StudentCourseGroupUpdateComponent>;
    let service: StudentCourseGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [StudentCourseGroupUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudentCourseGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentCourseGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentCourseGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentCourseGroup(123);
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
        const entity = new StudentCourseGroup();
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
