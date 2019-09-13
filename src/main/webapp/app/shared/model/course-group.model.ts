import { Moment } from 'moment';
import { ICourseInfo } from 'app/shared/model/course-info.model';

export interface ICourseGroup {
  id?: number;
  groupCode?: string;
  groupName?: string;
  groupCount?: number;
  dataTime?: Moment;
  course?: ICourseInfo;
}

export class CourseGroup implements ICourseGroup {
  constructor(
    public id?: number,
    public groupCode?: string,
    public groupName?: string,
    public groupCount?: number,
    public dataTime?: Moment,
    public course?: ICourseInfo
  ) {}
}
