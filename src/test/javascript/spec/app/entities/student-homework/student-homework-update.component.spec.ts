/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CoursegradeTestModule } from '../../../test.module';
import { StudentHomeworkUpdateComponent } from 'app/entities/student-homework/student-homework-update.component';
import { StudentHomeworkService } from 'app/entities/student-homework/student-homework.service';
import { StudentHomework } from 'app/shared/model/student-homework.model';

describe('Component Tests', () => {
  describe('StudentHomework Management Update Component', () => {
    let comp: StudentHomeworkUpdateComponent;
    let fixture: ComponentFixture<StudentHomeworkUpdateComponent>;
    let service: StudentHomeworkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CoursegradeTestModule],
        declarations: [StudentHomeworkUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudentHomeworkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentHomeworkUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentHomeworkService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentHomework(123);
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
        const entity = new StudentHomework();
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
