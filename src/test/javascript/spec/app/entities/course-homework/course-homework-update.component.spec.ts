/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseHomeworkUpdateComponent } from 'app/entities/course-homework/course-homework-update.component';
import { CourseHomeworkService } from 'app/entities/course-homework/course-homework.service';
import { CourseHomework } from 'app/shared/model/course-homework.model';

describe('Component Tests', () => {
  describe('CourseHomework Management Update Component', () => {
    let comp: CourseHomeworkUpdateComponent;
    let fixture: ComponentFixture<CourseHomeworkUpdateComponent>;
    let service: CourseHomeworkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseHomeworkUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CourseHomeworkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseHomeworkUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseHomeworkService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseHomework(123);
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
        const entity = new CourseHomework();
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
