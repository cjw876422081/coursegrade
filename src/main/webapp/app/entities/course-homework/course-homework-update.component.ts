import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICourseHomework, CourseHomework } from 'app/shared/model/course-homework.model';
import { CourseHomeworkService } from './course-homework.service';
import { ICoursePlan } from 'app/shared/model/course-plan.model';
import { CoursePlanService } from 'app/entities/course-plan';

@Component({
  selector: 'jhi-course-homework-update',
  templateUrl: './course-homework-update.component.html'
})
export class CourseHomeworkUpdateComponent implements OnInit {
  isSaving: boolean;

  courseplans: ICoursePlan[];

  editForm = this.fb.group({
    id: [],
    homeworkCode: [],
    homeworkMemo: [],
    homeworkTarget: [],
    homeworkGrade: [],
    homeworkDeadline: [],
    dataTime: [],
    plan: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected courseHomeworkService: CourseHomeworkService,
    protected coursePlanService: CoursePlanService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ courseHomework }) => {
      this.updateForm(courseHomework);
    });
    this.coursePlanService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICoursePlan[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICoursePlan[]>) => response.body)
      )
      .subscribe((res: ICoursePlan[]) => (this.courseplans = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(courseHomework: ICourseHomework) {
    this.editForm.patchValue({
      id: courseHomework.id,
      homeworkCode: courseHomework.homeworkCode,
      homeworkMemo: courseHomework.homeworkMemo,
      homeworkTarget: courseHomework.homeworkTarget,
      homeworkGrade: courseHomework.homeworkGrade,
      homeworkDeadline: courseHomework.homeworkDeadline != null ? courseHomework.homeworkDeadline.format(DATE_TIME_FORMAT) : null,
      dataTime: courseHomework.dataTime != null ? courseHomework.dataTime.format(DATE_TIME_FORMAT) : null,
      plan: courseHomework.plan
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const courseHomework = this.createFromForm();
    if (courseHomework.id !== undefined) {
      this.subscribeToSaveResponse(this.courseHomeworkService.update(courseHomework));
    } else {
      this.subscribeToSaveResponse(this.courseHomeworkService.create(courseHomework));
    }
  }

  private createFromForm(): ICourseHomework {
    return {
      ...new CourseHomework(),
      id: this.editForm.get(['id']).value,
      homeworkCode: this.editForm.get(['homeworkCode']).value,
      homeworkMemo: this.editForm.get(['homeworkMemo']).value,
      homeworkTarget: this.editForm.get(['homeworkTarget']).value,
      homeworkGrade: this.editForm.get(['homeworkGrade']).value,
      homeworkDeadline:
        this.editForm.get(['homeworkDeadline']).value != null
          ? moment(this.editForm.get(['homeworkDeadline']).value, DATE_TIME_FORMAT)
          : undefined,
      dataTime: this.editForm.get(['dataTime']).value != null ? moment(this.editForm.get(['dataTime']).value, DATE_TIME_FORMAT) : undefined,
      plan: this.editForm.get(['plan']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseHomework>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCoursePlanById(index: number, item: ICoursePlan) {
    return item.id;
  }
}
