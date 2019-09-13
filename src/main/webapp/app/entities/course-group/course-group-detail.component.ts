import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseGroup } from 'app/shared/model/course-group.model';

@Component({
  selector: 'jhi-course-group-detail',
  templateUrl: './course-group-detail.component.html'
})
export class CourseGroupDetailComponent implements OnInit {
  courseGroup: ICourseGroup;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ courseGroup }) => {
      this.courseGroup = courseGroup;
    });
  }

  previousState() {
    window.history.back();
  }
}
