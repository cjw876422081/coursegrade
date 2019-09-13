/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseInfoDeleteDialogComponent } from 'app/entities/course-info/course-info-delete-dialog.component';
import { CourseInfoService } from 'app/entities/course-info/course-info.service';

describe('Component Tests', () => {
  describe('CourseInfo Management Delete Component', () => {
    let comp: CourseInfoDeleteDialogComponent;
    let fixture: ComponentFixture<CourseInfoDeleteDialogComponent>;
    let service: CourseInfoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseInfoDeleteDialogComponent]
      })
        .overrideTemplate(CourseInfoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseInfoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseInfoService);
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
