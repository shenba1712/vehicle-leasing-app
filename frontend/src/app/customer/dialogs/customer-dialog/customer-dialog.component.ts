import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Customer} from "../../models/customer.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-customer-dialog',
  templateUrl: './customer-dialog.component.html',
  styleUrls: ['./customer-dialog.component.scss']
})
export class CustomerDialogComponent implements OnInit {

  customer: Customer;
  form!: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<CustomerDialogComponent>,
              private formBuilder: FormBuilder) {
    this.customer = data['customer'];
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstName: [this.customer?.firstName, Validators.required],
      lastName: [this.customer?.lastName, Validators.required],
      birthDate: [this.customer?.birthDate, Validators.required]
    });
  }

  save(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  close() : void {
    this.dialogRef.close();
  }

}
