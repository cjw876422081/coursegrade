import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IStudentHomework, StudentHomework } from 'app/shared/model/student-homework.model';
import { StudentHomeworkService } from './student-homework.service';
import { ICourseHomework } from 'app/shared/model/course-homework.model';
import { CourseHomeworkService } from 'app/entities/course-homework';

@Component({
  selector: 'jhi-student-homework-update',
  templateUrl: './student-homework-update.component.html'
})
export class StudentHomeworkUpdateComponent implements OnInit {
  isSaving: boolean;

  coursehomeworks: ICourseHomework[];

  editForm = this.fb.group({
    id: [],
    submitMemo: [],
    submitTime: [],
    readTime: [],
    grade: [],
    student: [],
    teacher: [],
    homework: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected studentHomeworkService: StudentHomeworkService,
    protected courseHomeworkService: CourseHomeworkService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ studentHomework }) => {
      this.updateForm(studentHomework);
    });
    this.courseHomeworkService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseHomework[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseHomework[]>) => response.body)
      )
      .subscribe((res: ICourseHomework[]) => (this.coursehomeworks = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(studentHomework: IStudentHomework) {
    this.editForm.patchValue({
      id: studentHomework.id,
      submitMemo: studentHomework.submitMemo,
      submitTime: studentHomework.submitTime != null ? studentHomework.submitTime.format(DATE_TIME_FORMAT) : null,
      readTime: studentHomework.readTime != null ? studentHomework.readTime.format(DATE_TIME_FORMAT) : null,
      grade: studentHomework.grade,
      student: studentHomework.student,
      teacher: studentHomework.teacher,
      homework: studentHomework.homework
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const studentHomework = this.createFromForm();
    if (studentHomework.id !== undefined) {
      this.subscribeToSaveResponse(this.studentHomeworkService.update(studentHomework));
    } else {
      this.subscribeToSaveResponse(this.studentHomeworkService.create(studentHomework));
    }
  }

  private createFromForm(): IStudentHomework {
    return {
      ...new StudentHomework(),
      id: this.editForm.get(['id']).value,
      submitMemo: this.editForm.get(['submitMemo']).value,
      submitTime:
        this.editForm.get(['submitTime']).value != null ? moment(this.editForm.get(['submitTime']).value, DATE_TIME_FORMAT) : undefined,
      readTime: this.editForm.get(['readTime']).value != null ? moment(this.editForm.get(['readTime']).value, DATE_TIME_FORMAT) : undefined,
      grade: this.editForm.get(['grade']).value,
      student: this.editForm.get(['student']).value,
      teacher: this.editForm.get(['teacher']).value,
      homework: this.editForm.get(['homework']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentHomework>>) {
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

  trackCourseHomeworkById(index: number, item: ICourseHomework) {
    return item.id;
  }
}
