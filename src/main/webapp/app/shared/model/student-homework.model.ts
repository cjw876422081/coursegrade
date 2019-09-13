import { Moment } from 'moment';
import { ICourseHomework } from 'app/shared/model/course-homework.model';

export interface IStudentHomework {
  id?: number;
  submitMemo?: string;
  submitTime?: Moment;
  readTime?: Moment;
  grade?: number;
  student?: string;
  teacher?: string;
  homework?: ICourseHomework;
}

export class StudentHomework implements IStudentHomework {
  constructor(
    public id?: number,
    public submitMemo?: string,
    public submitTime?: Moment,
    public readTime?: Moment,
    public grade?: number,
    public student?: string,
    public teacher?: string,
    public homework?: ICourseHomework
  ) {}
}
