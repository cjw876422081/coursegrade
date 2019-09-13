import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudentHomework } from 'app/shared/model/student-homework.model';
import { StudentHomeworkService } from './student-homework.service';

@Component({
  selector: 'jhi-student-homework-delete-dialog',
  templateUrl: './student-homework-delete-dialog.component.html'
})
export class StudentHomeworkDeleteDialogComponent {
  studentHomework: IStudentHomework;

  constructor(
    protected studentHomeworkService: StudentHomeworkService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.studentHomeworkService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'studentHomeworkListModification',
        content: 'Deleted an studentHomework'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-student-homework-delete-popup',
  template: ''
})
export class StudentHomeworkDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studentHomework }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StudentHomeworkDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.studentHomework = studentHomework;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/student-homework', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/student-homework', { outlets: { popup: null } }]);
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
