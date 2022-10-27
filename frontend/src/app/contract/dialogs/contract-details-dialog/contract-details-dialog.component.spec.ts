import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractDetailsDialogComponent } from './contract-details-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MatIconModule} from "@angular/material/icon";

describe('ContractDetailsDialogComponent', () => {
  let component: ContractDetailsDialogComponent;
  let fixture: ComponentFixture<ContractDetailsDialogComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [ ContractDetailsDialogComponent ],
      imports: [HttpClientTestingModule, MatDialogModule, MatIconModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContractDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
