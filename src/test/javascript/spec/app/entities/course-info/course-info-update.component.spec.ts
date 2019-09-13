/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { CourseInfoUpdateComponent } from 'app/entities/course-info/course-info-update.component';
import { CourseInfoService } from 'app/entities/course-info/course-info.service';
import { CourseInfo } from 'app/shared/model/course-info.model';

describe('Component Tests', () => {
  describe('CourseInfo Management Update Component', () => {
    let comp: CourseInfoUpdateComponent;
    let fixture: ComponentFixture<CourseInfoUpdateComponent>;
    let service: CourseInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [CourseInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CourseInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseInfo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseInfo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
