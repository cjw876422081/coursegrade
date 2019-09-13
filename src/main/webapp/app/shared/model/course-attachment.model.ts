import { Moment } from 'moment';
import { IStudentHomework } from 'app/shared/model/student-homework.model';
import { ICourseNote } from 'app/shared/model/course-note.model';

export interface ICourseAttachment {
  id?: number;
  attachmentType?: string;
  attachmentUse?: string;
  fileName?: string;
  originName?: string;
  filePath?: string;
  fileSize?: number;
  uploadTime?: Moment;
  fileUser?: string;
  homework?: IStudentHomework;
  note?: ICourseNote;
}

export class CourseAttachment implements ICourseAttachment {
  constructor(
    public id?: number,
    public attachmentType?: string,
    public attachmentUse?: string,
    public fileName?: string,
    public originName?: string,
    public filePath?: string,
    public fileSize?: number,
    public uploadTime?: Moment,
    public fileUser?: string,
    public homework?: IStudentHomework,
    public note?: ICourseNote
  ) {}
}
