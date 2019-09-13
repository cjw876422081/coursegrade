/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseNoteUpdateComponent } from 'app/entities/course-note/course-note-update.component';
import { CourseNoteService } from 'app/entities/course-note/course-note.service';
import { CourseNote } from 'app/shared/model/course-note.model';

describe('Component Tests', () => {
  describe('CourseNote Management Update Component', () => {
    let comp: CourseNoteUpdateComponent;
    let fixture: ComponentFixture<CourseNoteUpdateComponent>;
    let service: CourseNoteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseNoteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CourseNoteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseNoteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseNoteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseNote(123);
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
        const entity = new CourseNote();
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
