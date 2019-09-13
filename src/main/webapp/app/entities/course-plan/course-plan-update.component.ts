import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICoursePlan, CoursePlan } from 'app/shared/model/course-plan.model';
import { CoursePlanService } from './course-plan.service';
import { ICourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from 'app/entities/course-info';

@Component({
  selector: 'jhi-course-plan-update',
  templateUrl: './course-plan-update.component.html'
})
export class CoursePlanUpdateComponent implements OnInit {
  isSaving: boolean;

  courseplans: ICoursePlan[];

  courseinfos: ICourseInfo[];

  editForm = this.fb.group({
    id: [],
    planCode: [],
    planMemo: [],
    planTarget: [],
    planCount: [],
    planParentCode: [],
    dataTime: [],
    parentPlan: [],
    course: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected coursePlanService: CoursePlanService,
    protected courseInfoService: CourseInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ coursePlan }) => {
      this.updateForm(coursePlan);
    });
    this.coursePlanService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICoursePlan[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICoursePlan[]>) => response.body)
      )
      .subscribe((res: ICoursePlan[]) => (this.courseplans = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.courseInfoService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseInfo[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseInfo[]>) => response.body)
      )
      .subscribe((res: ICourseInfo[]) => (this.courseinfos = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(coursePlan: ICoursePlan) {
    this.editForm.patchValue({
      id: coursePlan.id,
      planCode: coursePlan.planCode,
      planMemo: coursePlan.planMemo,
      planTarget: coursePlan.planTarget,
      planCount: coursePlan.planCount,
      planParentCode: coursePlan.planParentCode,
      dataTime: coursePlan.dataTime != null ? coursePlan.dataTime.format(DATE_TIME_FORMAT) : null,
      parentPlan: coursePlan.parentPlan,
      course: coursePlan.course
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const coursePlan = this.createFromForm();
    if (coursePlan.id !== undefined) {
      this.subscribeToSaveResponse(this.coursePlanService.update(coursePlan));
    } else {
      this.subscribeToSaveResponse(this.coursePlanService.create(coursePlan));
    }
  }

  private createFromForm(): ICoursePlan {
    return {
      ...new CoursePlan(),
      id: this.editForm.get(['id']).value,
      planCode: this.editForm.get(['planCode']).value,
      planMemo: this.editForm.get(['planMemo']).value,
      planTarget: this.editForm.get(['planTarget']).value,
      planCount: this.editForm.get(['planCount']).value,
      planParentCode: this.editForm.get(['planParentCode']).value,
      dataTime: this.editForm.get(['dataTime']).value != null ? moment(this.editForm.get(['dataTime']).value, DATE_TIME_FORMAT) : undefined,
      parentPlan: this.editForm.get(['parentPlan']).value,
      course: this.editForm.get(['course']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoursePlan>>) {
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

  trackCourseInfoById(index: number, item: ICourseInfo) {
    return item.id;
  }
}
