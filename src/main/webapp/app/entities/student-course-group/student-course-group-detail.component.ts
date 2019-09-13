import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentCourseGroup } from 'app/shared/model/student-course-group.model';

@Component({
  selector: 'jhi-student-course-group-detail',
  templateUrl: './student-course-group-detail.component.html'
})
export class StudentCourseGroupDetailComponent implements OnInit {
  studentCourseGroup: IStudentCourseGroup;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studentCourseGroup }) => {
      this.studentCourseGroup = studentCourseGroup;
    });
  }

  previousState() {
    window.history.back();
  }
}
