import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICourseAttachment, CourseAttachment } from 'app/shared/model/course-attachment.model';
import { CourseAttachmentService } from './course-attachment.service';
import { IStudentHomework } from 'app/shared/model/student-homework.model';
import { StudentHomeworkService } from 'app/entities/student-homework';
import { ICourseNote } from 'app/shared/model/course-note.model';
import { CourseNoteService } from 'app/entities/course-note';

@Component({
  selector: 'jhi-course-attachment-update',
  templateUrl: './course-attachment-update.component.html'
})
export class CourseAttachmentUpdateComponent implements OnInit {
  isSaving: boolean;

  studenthomeworks: IStudentHomework[];

  coursenotes: ICourseNote[];

  editForm = this.fb.group({
    id: [],
    attachmentType: [],
    attachmentUse: [],
    fileName: [],
    originName: [],
    filePath: [],
    fileSize: [],
    uploadTime: [],
    fileUser: [],
    homework: [],
    note: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected courseAttachmentService: CourseAttachmentService,
    protected studentHomeworkService: StudentHomeworkService,
    protected courseNoteService: CourseNoteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ courseAttachment }) => {
      this.updateForm(courseAttachment);
    });
    this.studentHomeworkService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStudentHomework[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStudentHomework[]>) => response.body)
      )
      .subscribe((res: IStudentHomework[]) => (this.studenthomeworks = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.courseNoteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICourseNote[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICourseNote[]>) => response.body)
      )
      .subscribe((res: ICourseNote[]) => (this.coursenotes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(courseAttachment: ICourseAttachment) {
    this.editForm.patchValue({
      id: courseAttachment.id,
      attachmentType: courseAttachment.attachmentType,
      attachmentUse: courseAttachment.attachmentUse,
      fileName: courseAttachment.fileName,
      originName: courseAttachment.originName,
      filePath: courseAttachment.filePath,
      fileSize: courseAttachment.fileSize,
      uploadTime: courseAttachment.uploadTime != null ? courseAttachment.uploadTime.format(DATE_TIME_FORMAT) : null,
      fileUser: courseAttachment.fileUser,
      homework: courseAttachment.homework,
      note: courseAttachment.note
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const courseAttachment = this.createFromForm();
    if (courseAttachment.id !== undefined) {
      this.subscribeToSaveResponse(this.courseAttachmentService.update(courseAttachment));
    } else {
      this.subscribeToSaveResponse(this.courseAttachmentService.create(courseAttachment));
    }
  }

  private createFromForm(): ICourseAttachment {
    return {
      ...new CourseAttachment(),
      id: this.editForm.get(['id']).value,
      attachmentType: this.editForm.get(['attachmentType']).value,
      attachmentUse: this.editForm.get(['attachmentUse']).value,
      fileName: this.editForm.get(['fileName']).value,
      originName: this.editForm.get(['originName']).value,
      filePath: this.editForm.get(['filePath']).value,
      fileSize: this.editForm.get(['fileSize']).value,
      uploadTime:
        this.editForm.get(['uploadTime']).value != null ? moment(this.editForm.get(['uploadTime']).value, DATE_TIME_FORMAT) : undefined,
      fileUser: this.editForm.get(['fileUser']).value,
      homework: this.editForm.get(['homework']).value,
      note: this.editForm.get(['note']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseAttachment>>) {
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

  trackStudentHomeworkById(index: number, item: IStudentHomework) {
    return item.id;
  }

  trackCourseNoteById(index: number, item: ICourseNote) {
    return item.id;
  }
}
