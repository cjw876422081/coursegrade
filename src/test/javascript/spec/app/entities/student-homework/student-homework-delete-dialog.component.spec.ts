/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { StudentHomeworkDeleteDialogComponent } from 'app/entities/student-homework/student-homework-delete-dialog.component';
import { StudentHomeworkService } from 'app/entities/student-homework/student-homework.service';

describe('Component Tests', () => {
  describe('StudentHomework Management Delete Component', () => {
    let comp: StudentHomeworkDeleteDialogComponent;
    let fixture: ComponentFixture<StudentHomeworkDeleteDialogComponent>;
    let service: StudentHomeworkService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [StudentHomeworkDeleteDialogComponent]
      })
        .overrideTemplate(StudentHomeworkDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentHomeworkDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentHomeworkService);
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
