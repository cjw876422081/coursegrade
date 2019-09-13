import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IStudentCourseGroup, StudentCourseGroup } from 'app/shared/model/student-course-group.model';
import { StudentCourseGroupService } from './student-course-group.service';
import { ICourseGroup } from 'app/shared/model/course-group.model';
import { CourseGroupService } from 'app/entities/course-group';

@Component({
  selector: 'jhi-student-course-group-update',
  templateUrl: './student-course-group-update.component.html'
})
export class StudentCourseGroupUpdateComponent implements OnInit {
  isSaving: boolean;

  coursegroups: ICourseGroup[];

  editForm = this.fb.group({
    id: [],
    student: [],
    joinTime: [],
    group: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studentCourseGroupService: StudentCourseGroupService,
    protected courseGroupService: CourseGroupService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studentCourseGroup }) => {
      this.updateForm(studentCourseGroup);
    });
    this.courseGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseGroup[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseGroup[]>) => response.body)
      )
      .subscribe((res: ICourseGroup[]) => (this.coursegroups = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studentCourseGroup: IStudentCourseGroup) {
    this.editForm.patchValue({
      id: studentCourseGroup.id,
      student: studentCourseGroup.student,
      joinTime: studentCourseGroup.joinTime != null ? studentCourseGroup.joinTime.format(DATE_TIME_FORMAT) : null,
      group: studentCourseGroup.group
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studentCourseGroup = this.createFromForm();
    if (studentCourseGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.studentCourseGroupService.update(studentCourseGroup));
    } else {
      this.subscribeToSaveResponse(this.studentCourseGroupService.create(studentCourseGroup));
    }
  }

  private createFromForm(): IStudentCourseGroup {
    return {
      ...new StudentCourseGroup(),
      id: this.editForm.get(['id']).value,
      student: this.editForm.get(['student']).value,
      joinTime: this.editForm.get(['joinTime']).value != null ? moment(this.editForm.get(['joinTime']).value, DATE_TIME_FORMAT) : undefined,
      group: this.editForm.get(['group']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentCourseGroup>>) {
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

  trackCourseGroupById(index: number, item: ICourseGroup) {
    return item.id;
  }
}
