import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseHomework } from 'app/shared/model/course-homework.model';

@Component({
  selector: 'jhi-course-homework-detail',
  templateUrl: './course-homework-detail.component.html'
})
export class CourseHomeworkDetailComponent implements OnInit {
  courseHomework: ICourseHomework;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseHomework }) => {
      this.courseHomework = courseHomework;
    });
  }

  previousState() {
    window.history.back();
  }
}
