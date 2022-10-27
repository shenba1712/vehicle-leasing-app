import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Contract} from "../../models/contract.model";
import {
  CustomerDetailsDialogComponent
} from "../../../customer/dialogs/customer-details-dialog/customer-details-dialog.component";
import {
  VehicleDetailsDialogComponent
} from "../../../vehicle/dialogs/vehicle-details-dialog/vehicle-details-dialog.component";

@Component({
  selector: 'app-contract-details-dialog',
  templateUrl: './contract-details-dialog.component.html'
})
export class ContractDetailsDialogComponent {

  contract!: Contract;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private dialogRef: MatDialogRef<ContractDetailsDialogComponent>) {
    this.contract = data['contract'];
  }

  close() {
    this.dialogRef.close();
  }

  openCustomerDetailsDialog(): void {
    this.dialog.open(CustomerDetailsDialogComponent, {
      width: '500px',
      data: {id:  this.contract.customer.id}
    });
  }

  openVehicleDetailsDialog(): void {
    this.dialog.open(VehicleDetailsDialogComponent, {
      width: '500px',
      data: {id:  this.contract.vehicle.id}
    });
  }

}
