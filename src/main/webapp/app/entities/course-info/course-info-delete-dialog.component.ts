import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from './course-info.service';

@Component({
  selector: 'jhi-course-info-delete-dialog',
  templateUrl: './course-info-delete-dialog.component.html'
})
export class CourseInfoDeleteDialogComponent {
  courseInfo: ICourseInfo;

  constructor(
    protected courseInfoService: CourseInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseInfoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'courseInfoListModification',
        content: 'Deleted an courseInfo'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-info-delete-popup',
  template: ''
})
export class CourseInfoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseInfo }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CourseInfoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.courseInfo = courseInfo;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-info', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-info', { outlets: { popup: null } }]);
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
