import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoursePlan } from 'app/shared/model/course-plan.model';
import { CoursePlanService } from './course-plan.service';

@Component({
  selector: 'jhi-course-plan-delete-dialog',
  templateUrl: './course-plan-delete-dialog.component.html'
})
export class CoursePlanDeleteDialogComponent {
  coursePlan: ICoursePlan;

  constructor(
    protected coursePlanService: CoursePlanService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.coursePlanService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'coursePlanListModification',
        content: 'Deleted an coursePlan'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-plan-delete-popup',
  template: ''
})
export class CoursePlanDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ coursePlan }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CoursePlanDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.coursePlan = coursePlan;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-plan', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-plan', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
