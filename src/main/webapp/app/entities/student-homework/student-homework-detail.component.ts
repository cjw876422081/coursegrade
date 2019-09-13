import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentHomework } from 'app/shared/model/student-homework.model';

@Component({
  selector: 'jhi-student-homework-detail',
  templateUrl: './student-homework-detail.component.html'
})
export class StudentHomeworkDetailComponent implements OnInit {
  studentHomework: IStudentHomework;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ studentHomework }) => {
      this.studentHomework = studentHomework;
    });
  }

  previousState() {
    window.history.back();
  }
}
