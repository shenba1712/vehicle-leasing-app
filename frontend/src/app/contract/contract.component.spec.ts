import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractComponent } from './contract.component';
import {ContractService} from "./service/contract.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MatDialogModule} from "@angular/material/dialog";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

describe('ContractComponent', () => {
  let component: ContractComponent;
  let fixture: ComponentFixture<ContractComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ContractComponent ],
      imports: [HttpClientTestingModule, MatDialogModule, MatProgressSpinnerModule],
      providers: [ContractService]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
