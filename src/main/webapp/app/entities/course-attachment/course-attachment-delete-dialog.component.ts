import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseAttachment } from 'app/shared/model/course-attachment.model';
import { CourseAttachmentService } from './course-attachment.service';

@Component({
  selector: 'jhi-course-attachment-delete-dialog',
  templateUrl: './course-attachment-delete-dialog.component.html'
})
export class CourseAttachmentDeleteDialogComponent {
  courseAttachment: ICourseAttachment;

  constructor(
    protected courseAttachmentService: CourseAttachmentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseAttachmentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'courseAttachmentListModification',
        content: 'Deleted an courseAttachment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-attachment-delete-popup',
  template: ''
})
export class CourseAttachmentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseAttachment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CourseAttachmentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.courseAttachment = courseAttachment;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-attachment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-attachment', { outlets: { popup: null } }]);
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
