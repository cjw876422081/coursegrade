import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseHomework } from 'app/shared/model/course-homework.model';
import { CourseHomeworkService } from './course-homework.service';

@Component({
  selector: 'jhi-course-homework-delete-dialog',
  templateUrl: './course-homework-delete-dialog.component.html'
})
export class CourseHomeworkDeleteDialogComponent {
  courseHomework: ICourseHomework;

  constructor(
    protected courseHomeworkService: CourseHomeworkService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseHomeworkService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'courseHomeworkListModification',
        content: 'Deleted an courseHomework'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-homework-delete-popup',
  template: ''
})
export class CourseHomeworkDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseHomework }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CourseHomeworkDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.courseHomework = courseHomework;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-homework', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-homework', { outlets: { popup: null } }]);
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
