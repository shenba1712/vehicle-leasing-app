import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleDetailsDialogComponent } from './vehicle-details-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('VehicleDetailsDialogComponent', () => {
  let component: VehicleDetailsDialogComponent;
  let fixture: ComponentFixture<VehicleDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VehicleDetailsDialogComponent ],
      imports: [
        HttpClientTestingModule,
        MatDialogModule
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VehicleDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
