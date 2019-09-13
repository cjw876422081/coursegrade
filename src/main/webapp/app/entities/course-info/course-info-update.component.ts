import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ICourseInfo, CourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from './course-info.service';

@Component({
  selector: 'jhi-course-info-update',
  templateUrl: './course-info-update.component.html'
})
export class CourseInfoUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    courseCode: [],
    courseName: [],
    courseCount: [],
    courseWeekCount: [],
    courseMemo: [],
    dataTime: [],
    courseUser: []
  });

  constructor(protected courseInfoService: CourseInfoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ courseInfo }) => {
      this.updateForm(courseInfo);
    });
  }

  updateForm(courseInfo: ICourseInfo) {
    this.editForm.patchValue({
      id: courseInfo.id,
      courseCode: courseInfo.courseCode,
      courseName: courseInfo.courseName,
      courseCount: courseInfo.courseCount,
      courseWeekCount: courseInfo.courseWeekCount,
      courseMemo: courseInfo.courseMemo,
      dataTime: courseInfo.dataTime != null ? courseInfo.dataTime.format(DATE_TIME_FORMAT) : null,
      courseUser: courseInfo.courseUser
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const courseInfo = this.createFromForm();
    if (courseInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.courseInfoService.update(courseInfo));
    } else {
      this.subscribeToSaveResponse(this.courseInfoService.create(courseInfo));
    }
  }

  private createFromForm(): ICourseInfo {
    return {
      ...new CourseInfo(),
      id: this.editForm.get(['id']).value,
      courseCode: this.editForm.get(['courseCode']).value,
      courseName: this.editForm.get(['courseName']).value,
      courseCount: this.editForm.get(['courseCount']).value,
      courseWeekCount: this.editForm.get(['courseWeekCount']).value,
      courseMemo: this.editForm.get(['courseMemo']).value,
      dataTime: this.editForm.get(['dataTime']).value != null ? moment(this.editForm.get(['dataTime']).value, DATE_TIME_FORMAT) : undefined,
      courseUser: this.editForm.get(['courseUser']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseInfo>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
