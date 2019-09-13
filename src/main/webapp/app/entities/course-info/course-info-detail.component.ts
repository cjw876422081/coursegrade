import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseInfo } from 'app/shared/model/course-info.model';

@Component({
  selector: 'jhi-course-info-detail',
  templateUrl: './course-info-detail.component.html'
})
export class CourseInfoDetailComponent implements OnInit {
  courseInfo: ICourseInfo;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseInfo }) => {
      this.courseInfo = courseInfo;
    });
  }

  previousState() {
    window.history.back();
  }
}
