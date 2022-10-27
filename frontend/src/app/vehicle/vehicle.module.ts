import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VehicleRoutingModule } from './vehicle-routing.module';
import { VehicleComponent } from './vehicle.component';
import { VehicleDialogComponent } from './dialogs/vehicle-dialog/vehicle-dialog.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTableModule} from "@angular/material/table";
import {MatSelectModule} from "@angular/material/select";
import { VehicleDetailsDialogComponent } from './dialogs/vehicle-details-dialog/vehicle-details-dialog.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";


@NgModule({
  declarations: [
    VehicleComponent,
    VehicleDialogComponent,
    VehicleDetailsDialogComponent
  ],
  imports: [
    CommonModule,
    VehicleRoutingModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatPaginatorModule,
    MatDialogModule,
    MatTableModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ]
})
export class VehicleModule { }
