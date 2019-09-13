/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseAttachmentUpdateComponent } from 'app/entities/course-attachment/course-attachment-update.component';
import { CourseAttachmentService } from 'app/entities/course-attachment/course-attachment.service';
import { CourseAttachment } from 'app/shared/model/course-attachment.model';

describe('Component Tests', () => {
  describe('CourseAttachment Management Update Component', () => {
    let comp: CourseAttachmentUpdateComponent;
    let fixture: ComponentFixture<CourseAttachmentUpdateComponent>;
    let service: CourseAttachmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseAttachmentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CourseAttachmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseAttachmentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseAttachmentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseAttachment(123);
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
        const entity = new CourseAttachment();
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
