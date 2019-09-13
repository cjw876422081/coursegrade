/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseHomeworkDeleteDialogComponent } from 'app/entities/course-homework/course-homework-delete-dialog.component';
import { CourseHomeworkService } from 'app/entities/course-homework/course-homework.service';

describe('Component Tests', () => {
  describe('CourseHomework Management Delete Component', () => {
    let comp: CourseHomeworkDeleteDialogComponent;
    let fixture: ComponentFixture<CourseHomeworkDeleteDialogComponent>;
    let service: CourseHomeworkService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseHomeworkDeleteDialogComponent]
      })
        .overrideTemplate(CourseHomeworkDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseHomeworkDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseHomeworkService);
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
