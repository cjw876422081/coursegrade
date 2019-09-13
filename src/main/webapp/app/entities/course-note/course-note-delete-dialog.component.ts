import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseNote } from 'app/shared/model/course-note.model';
import { CourseNoteService } from './course-note.service';

@Component({
  selector: 'jhi-course-note-delete-dialog',
  templateUrl: './course-note-delete-dialog.component.html'
})
export class CourseNoteDeleteDialogComponent {
  courseNote: ICourseNote;

  constructor(
    protected courseNoteService: CourseNoteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseNoteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'courseNoteListModification',
        content: 'Deleted an courseNote'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-course-note-delete-popup',
  template: ''
})
export class CourseNoteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseNote }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CourseNoteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.courseNote = courseNote;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/course-note', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/course-note', { outlets: { popup: null } }]);
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
