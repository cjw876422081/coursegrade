import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICourseGroup, CourseGroup } from 'app/shared/model/course-group.model';
import { CourseGroupService } from './course-group.service';
import { ICourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from 'app/entities/course-info';

@Component({
  selector: 'jhi-course-group-update',
  templateUrl: './course-group-update.component.html'
})
export class CourseGroupUpdateComponent implements OnInit {
  isSaving: boolean;

  courseinfos: ICourseInfo[];

  editForm = this.fb.group({
    id: [],
    groupCode: [],
    groupName: [],
    groupCount: [],
    dataTime: [],
    course: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected courseGroupService: CourseGroupService,
    protected courseInfoService: CourseInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ courseGroup }) => {
      this.updateForm(courseGroup);
    });
    this.courseInfoService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseInfo[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseInfo[]>) => response.body)
      )
      .subscribe((res: ICourseInfo[]) => (this.courseinfos = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(courseGroup: ICourseGroup) {
    this.editForm.patchValue({
      id: courseGroup.id,
      groupCode: courseGroup.groupCode,
      groupName: courseGroup.groupName,
      groupCount: courseGroup.groupCount,
      dataTime: courseGroup.dataTime != null ? courseGroup.dataTime.format(DATE_TIME_FORMAT) : null,
      course: courseGroup.course
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const courseGroup = this.createFromForm();
    if (courseGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.courseGroupService.update(courseGroup));
    } else {
      this.subscribeToSaveResponse(this.courseGroupService.create(courseGroup));
    }
  }

  private createFromForm(): ICourseGroup {
    return {
      ...new CourseGroup(),
      id: this.editForm.get(['id']).value,
      groupCode: this.editForm.get(['groupCode']).value,
      groupName: this.editForm.get(['groupName']).value,
      groupCount: this.editForm.get(['groupCount']).value,
      dataTime: this.editForm.get(['dataTime']).value != null ? moment(this.editForm.get(['dataTime']).value, DATE_TIME_FORMAT) : undefined,
      course: this.editForm.get(['course']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseGroup>>) {
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

  trackCourseInfoById(index: number, item: ICourseInfo) {
    return item.id;
  }
}
