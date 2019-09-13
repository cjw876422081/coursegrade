import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudentCourseGroup } from 'app/shared/model/student-course-group.model';
import { StudentCourseGroupService } from './student-course-group.service';

@Component({
  selector: 'jhi-student-course-group-delete-dialog',
  templateUrl: './student-course-group-delete-dialog.component.html'
})
export class StudentCourseGroupDeleteDialogComponent {
  studentCourseGroup: IStudentCourseGroup;

  constructor(
    protected studentCourseGroupService: StudentCourseGroupService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studentCourseGroupService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studentCourseGroupListModification',
        content: 'Deleted an studentCourseGroup'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-student-course-group-delete-popup',
  template: ''
})
export class StudentCourseGroupDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studentCourseGroup }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudentCourseGroupDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studentCourseGroup = studentCourseGroup;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/student-course-group', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/student-course-group', { outlets: { popup: null } }]);
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
