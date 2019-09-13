import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseNote } from 'app/shared/model/course-note.model';

@Component({
  selector: 'jhi-course-note-detail',
  templateUrl: './course-note-detail.component.html'
})
export class CourseNoteDetailComponent implements OnInit {
  courseNote: ICourseNote;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseNote }) => {
      this.courseNote = courseNote;
    });
  }

  previousState() {
    window.history.back();
  }
}
