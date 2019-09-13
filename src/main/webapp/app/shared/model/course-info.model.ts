import { Moment } from 'moment';

export interface ICourseInfo {
  id?: number;
  courseCode?: string;
  courseName?: string;
  courseCount?: number;
  courseWeekCount?: number;
  courseMemo?: string;
  dataTime?: Moment;
  courseUser?: string;
}

export class CourseInfo implements ICourseInfo {
  constructor(
    public id?: number,
    public courseCode?: string,
    public courseName?: string,
    public courseCount?: number,
    public courseWeekCount?: number,
    public courseMemo?: string,
    public dataTime?: Moment,
    public courseUser?: string
  ) {}
}
