import { Moment } from 'moment';
import { ICoursePlan } from 'app/shared/model/course-plan.model';
import { ICourseInfo } from 'app/shared/model/course-info.model';

export interface ICoursePlan {
  id?: number;
  planCode?: number;
  planMemo?: string;
  planTarget?: string;
  planCount?: number;
  planParentCode?: number;
  dataTime?: Moment;
  parentPlan?: ICoursePlan;
  course?: ICourseInfo;
}

export class CoursePlan implements ICoursePlan {
  constructor(
    public id?: number,
    public planCode?: number,
    public planMemo?: string,
    public planTarget?: string,
    public planCount?: number,
    public planParentCode?: number,
    public dataTime?: Moment,
    public parentPlan?: ICoursePlan,
    public course?: ICourseInfo
  ) {}
}
