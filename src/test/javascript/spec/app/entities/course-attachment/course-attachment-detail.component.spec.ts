/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseAttachmentDetailComponent } from 'app/entities/course-attachment/course-attachment-detail.component';
import { CourseAttachment } from 'app/shared/model/course-attachment.model';

describe('Component Tests', () => {
  describe('CourseAttachment Management Detail Component', () => {
    let comp: CourseAttachmentDetailComponent;
    let fixture: ComponentFixture<CourseAttachmentDetailComponent>;
    const route = ({ data: of({ courseAttachment: new CourseAttachment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseAttachmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CourseAttachmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseAttachmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseAttachment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
