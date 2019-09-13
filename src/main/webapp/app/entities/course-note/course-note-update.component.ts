import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICourseNote, CourseNote } from 'app/shared/model/course-note.model';
import { CourseNoteService } from './course-note.service';
import { ICourseInfo } from 'app/shared/model/course-info.model';
import { CourseInfoService } from 'app/entities/course-info';
import { ICoursePlan } from 'app/shared/model/course-plan.model';
import { CoursePlanService } from 'app/entities/course-plan';
import { ICourseHomework } from 'app/shared/model/course-homework.model';
import { CourseHomeworkService } from 'app/entities/course-homework';

@Component({
  selector: 'jhi-course-note-update',
  templateUrl: './course-note-update.component.html'
})
export class CourseNoteUpdateComponent implements OnInit {
  isSaving: boolean;

  courseinfos: ICourseInfo[];

  courseplans: ICoursePlan[];

  coursehomeworks: ICourseHomework[];

  coursenotes: ICourseNote[];

  editForm = this.fb.group({
    id: [],
    noteType: [],
    noteMemo: [],
    noteTime: [],
    publishUser: [],
    course: [],
    plan: [],
    homework: [],
    parentNote: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected courseNoteService: CourseNoteService,
    protected courseInfoService: CourseInfoService,
    protected coursePlanService: CoursePlanService,
    protected courseHomeworkService: CourseHomeworkService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ courseNote }) => {
      this.updateForm(courseNote);
    });
    this.courseInfoService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseInfo[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseInfo[]>) => response.body)
      )
      .subscribe((res: ICourseInfo[]) => (this.courseinfos = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.coursePlanService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICoursePlan[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICoursePlan[]>) => response.body)
      )
      .subscribe((res: ICoursePlan[]) => (this.courseplans = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.courseHomeworkService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseHomework[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseHomework[]>) => response.body)
      )
      .subscribe((res: ICourseHomework[]) => (this.coursehomeworks = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.courseNoteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseNote[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseNote[]>) => response.body)
      )
      .subscribe((res: ICourseNote[]) => (this.coursenotes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(courseNote: ICourseNote) {
    this.editForm.patchValue({
      id: courseNote.id,
      noteType: courseNote.noteType,
      noteMemo: courseNote.noteMemo,
      noteTime: courseNote.noteTime != null ? courseNote.noteTime.format(DATE_TIME_FORMAT) : null,
      publishUser: courseNote.publishUser,
      course: courseNote.course,
      plan: courseNote.plan,
      homework: courseNote.homework,
      parentNote: courseNote.parentNote
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const courseNote = this.createFromForm();
    if (courseNote.id !== undefined) {
      this.subscribeToSaveResponse(this.courseNoteService.update(courseNote));
    } else {
      this.subscribeToSaveResponse(this.courseNoteService.create(courseNote));
    }
  }

  private createFromForm(): ICourseNote {
    return {
      ...new CourseNote(),
      id: this.editForm.get(['id']).value,
      noteType: this.editForm.get(['noteType']).value,
      noteMemo: this.editForm.get(['noteMemo']).value,
      noteTime: this.editForm.get(['noteTime']).value != null ? moment(this.editForm.get(['noteTime']).value, DATE_TIME_FORMAT) : undefined,
      publishUser: this.editForm.get(['publishUser']).value,
      course: this.editForm.get(['course']).value,
      plan: this.editForm.get(['plan']).value,
      homework: this.editForm.get(['homework']).value,
      parentNote: this.editForm.get(['parentNote']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseNote>>) {
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

  trackCoursePlanById(index: number, item: ICoursePlan) {
    return item.id;
  }

  trackCourseHomeworkById(index: number, item: ICourseHomework) {
    return item.id;
  }

  trackCourseNoteById(index: number, item: ICourseNote) {
    return item.id;
  }
}
