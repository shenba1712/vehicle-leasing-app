import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContractRoutingModule } from './contract-routing.module';
import { ContractComponent } from './contract.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTableModule} from "@angular/material/table";
import {ContractService} from "./service/contract.service";
import { ContractDialogComponent } from './dialogs/contract-dialog/contract-dialog.component';
import { ContractDetailsDialogComponent } from './dialogs/contract-details-dialog/contract-details-dialog.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";


@NgModule({
  declarations: [
    ContractComponent,
    ContractDialogComponent,
    ContractDetailsDialogComponent
  ],
  imports: [
    CommonModule,
    ContractRoutingModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatPaginatorModule,
    MatDialogModule,
    MatTableModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule
  ],
  providers: [
    ContractService
  ]
})
export class ContractModule { }
