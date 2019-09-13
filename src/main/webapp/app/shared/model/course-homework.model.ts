import { Moment } from 'moment';
import { ICoursePlan } from 'app/shared/model/course-plan.model';

export interface ICourseHomework {
  id?: number;
  homeworkCode?: string;
  homeworkMemo?: string;
  homeworkTarget?: string;
  homeworkGrade?: number;
  homeworkDeadline?: Moment;
  dataTime?: Moment;
  plan?: ICoursePlan;
}

export class CourseHomework implements ICourseHomework {
  constructor(
    public id?: number,
    public homeworkCode?: string,
    public homeworkMemo?: string,
    public homeworkTarget?: string,
    public homeworkGrade?: number,
    public homeworkDeadline?: Moment,
    public dataTime?: Moment,
    public plan?: ICoursePlan
  ) {}
}
