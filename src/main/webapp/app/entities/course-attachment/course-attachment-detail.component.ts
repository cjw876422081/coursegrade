import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseAttachment } from 'app/shared/model/course-attachment.model';

@Component({
  selector: 'jhi-course-attachment-detail',
  templateUrl: './course-attachment-detail.component.html'
})
export class CourseAttachmentDetailComponent implements OnInit {
  courseAttachment: ICourseAttachment;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseAttachment }) => {
      this.courseAttachment = courseAttachment;
    });
  }

  previousState() {
    window.history.back();
  }
}
