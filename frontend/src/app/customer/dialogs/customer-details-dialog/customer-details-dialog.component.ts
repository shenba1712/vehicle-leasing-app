import {Component, Inject, OnInit} from '@angular/core';
import {Customer} from "../../models/customer.model";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CustomerService} from "../../service/customer.service";

@Component({
  selector: 'app-customer-details-dialog',
  templateUrl: './customer-details-dialog.component.html'
})
export class CustomerDetailsDialogComponent implements OnInit {

  customerId: number;
  customer?: Customer;


  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<CustomerDetailsDialogComponent>,
              private customerService: CustomerService) {
    this.customerId = data['id'];
  }

  ngOnInit(): void {
    this.customerService.getCustomer(this.customerId).subscribe(customer => {
      this.customer = customer;
    })
  }

  close() {
    this.dialogRef.close();
  }

}
