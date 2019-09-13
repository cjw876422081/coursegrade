/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseNoteDetailComponent } from 'app/entities/course-note/course-note-detail.component';
import { CourseNote } from 'app/shared/model/course-note.model';

describe('Component Tests', () => {
  describe('CourseNote Management Detail Component', () => {
    let comp: CourseNoteDetailComponent;
    let fixture: ComponentFixture<CourseNoteDetailComponent>;
    const route = ({ data: of({ courseNote: new CourseNote(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseNoteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CourseNoteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseNoteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseNote).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
