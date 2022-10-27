import {Component, Inject, OnInit} from '@angular/core';
import {Vehicle} from "../../models/vehicle.model";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {VehicleService} from "../../service/vehicle.service";

@Component({
  selector: 'app-vehicle-details-dialog',
  templateUrl: './vehicle-details-dialog.component.html'
})
export class VehicleDetailsDialogComponent implements OnInit {

  vehicleId: number;
  vehicle?: Vehicle;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<VehicleDetailsDialogComponent>,
              private vehicleService: VehicleService) {
    this.vehicleId = data['id'];
  }

  ngOnInit(): void {
    this.vehicleService.getVehicle(this.vehicleId).subscribe(vehicle => {
      this.vehicle = vehicle;
    })
  }

  close() {
    this.dialogRef.close();
  }

}
