import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoursePlan } from 'app/shared/model/course-plan.model';

@Component({
  selector: 'jhi-course-plan-detail',
  templateUrl: './course-plan-detail.component.html'
})
export class CoursePlanDetailComponent implements OnInit {
  coursePlan: ICoursePlan;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ coursePlan }) => {
      this.coursePlan = coursePlan;
    });
  }

  previousState() {
    window.history.back();
  }
}
