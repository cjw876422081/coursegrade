/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseNoteDeleteDialogComponent } from 'app/entities/course-note/course-note-delete-dialog.component';
import { CourseNoteService } from 'app/entities/course-note/course-note.service';

describe('Component Tests', () => {
  describe('CourseNote Management Delete Component', () => {
    let comp: CourseNoteDeleteDialogComponent;
    let fixture: ComponentFixture<CourseNoteDeleteDialogComponent>;
    let service: CourseNoteService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseNoteDeleteDialogComponent]
      })
        .overrideTemplate(CourseNoteDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseNoteDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseNoteService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
