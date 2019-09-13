/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CoursegradeTestModule } from '../../../test.module';
import { CoursePlanDeleteDialogComponent } from 'app/entities/course-plan/course-plan-delete-dialog.component';
import { CoursePlanService } from 'app/entities/course-plan/course-plan.service';

describe('Component Tests', () => {
  describe('CoursePlan Management Delete Component', () => {
    let comp: CoursePlanDeleteDialogComponent;
    let fixture: ComponentFixture<CoursePlanDeleteDialogComponent>;
    let service: CoursePlanService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CoursePlanDeleteDialogComponent]
      })
        .overrideTemplate(CoursePlanDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoursePlanDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CoursePlanService);
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
