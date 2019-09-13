import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseGroup } from 'app/shared/model/course-group.model';
import { CourseGroupService } from './course-group.service';

@Component({
  selector: 'jhi-course-group-delete-dialog',
  templateUrl: './course-group-delete-dialog.component.html'
})
export class CourseGroupDeleteDialogComponent {
  courseGroup: ICourseGroup;

  constructor(
    protected courseGroupService: CourseGroupService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseGroupService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'courseGroupListModification',
        content: 'Deleted an courseGroup'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-group-delete-popup',
  template: ''
})
export class CourseGroupDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseGroup }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CourseGroupDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.courseGroup = courseGroup;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-group', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-group', { outlets: { popup: null } }]);
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
