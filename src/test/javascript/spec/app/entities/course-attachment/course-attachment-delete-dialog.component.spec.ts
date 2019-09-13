/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseAttachmentDeleteDialogComponent } from 'app/entities/course-attachment/course-attachment-delete-dialog.component';
import { CourseAttachmentService } from 'app/entities/course-attachment/course-attachment.service';

describe('Component Tests', () => {
  describe('CourseAttachment Management Delete Component', () => {
    let comp: CourseAttachmentDeleteDialogComponent;
    let fixture: ComponentFixture<CourseAttachmentDeleteDialogComponent>;
    let service: CourseAttachmentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseAttachmentDeleteDialogComponent]
      })
        .overrideTemplate(CourseAttachmentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseAttachmentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseAttachmentService);
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
