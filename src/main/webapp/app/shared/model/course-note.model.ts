import { Moment } from 'moment';
import { ICourseInfo } from 'app/shared/model/course-info.model';
import { ICoursePlan } from 'app/shared/model/course-plan.model';
import { ICourseHomework } from 'app/shared/model/course-homework.model';
import { ICourseNote } from 'app/shared/model/course-note.model';

export interface ICourseNote {
  id?: number;
  noteType?: string;
  noteMemo?: string;
  noteTime?: Moment;
  publishUser?: string;
  course?: ICourseInfo;
  plan?: ICoursePlan;
  homework?: ICourseHomework;
  parentNote?: ICourseNote;
}

export class CourseNote implements ICourseNote {
  constructor(
    public id?: number,
    public noteType?: string,
    public noteMemo?: string,
    public noteTime?: Moment,
    public publishUser?: string,
    public course?: ICourseInfo,
    public plan?: ICoursePlan,
    public homework?: ICourseHomework,
    public parentNote?: ICourseNote
  ) {}
}
