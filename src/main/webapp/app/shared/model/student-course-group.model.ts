import { Moment } from 'moment';
import { ICourseGroup } from 'app/shared/model/course-group.model';

export interface IStudentCourseGroup {
  id?: number;
  student?: string;
  joinTime?: Moment;
  group?: ICourseGroup;
}

export class StudentCourseGroup implements IStudentCourseGroup {
  constructor(public id?: number, public student?: string, public joinTime?: Moment, public group?: ICourseGroup) {}
}
