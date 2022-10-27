import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerDetailsDialogComponent } from './customer-details-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CustomerDetailsDialogComponent', () => {
  let component: CustomerDetailsDialogComponent;
  let fixture: ComponentFixture<CustomerDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomerDetailsDialogComponent ],
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

    fixture = TestBed.createComponent(CustomerDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
