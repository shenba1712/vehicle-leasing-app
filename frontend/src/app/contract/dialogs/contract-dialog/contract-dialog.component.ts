import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Contract} from "../../models/contract.model";
import {LeasingCustomer} from "../../models/leasing-customer.model";
import {ContractService} from "../../service/contract.service";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {LeasingVehicle} from "../../models/leasing-vehicle.model";

@Component({
  selector: 'app-contract-dialog',
  templateUrl: './contract-dialog.component.html',
  styleUrls: ['./contract-dialog.component.scss']
})
export class ContractDialogComponent implements OnInit {

  contract: Contract;
  form!: FormGroup;
  isEdit: boolean = false;

  selectedCustomer!: LeasingCustomer | undefined;
  filteredCustomers?: LeasingCustomer[];

  selectedVehicle!: LeasingVehicle | undefined;
  filteredVehicles?: LeasingVehicle[];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private contractService: ContractService,
              private dialogRef: MatDialogRef<ContractDialogComponent>,
              private formBuilder: FormBuilder) {
    this.contract = data['contract'];
    this.isEdit = data['isEdit'];
  }

  ngOnInit(): void {
    this.selectedCustomer = this.contract?.customer;
    this.selectedVehicle = this.contract?.vehicle;
    this.form = this.formBuilder.group({
      monthlyRate: [this.contract?.monthlyRate, Validators.required],
      customerId: [this.contract?.customer?.id, Validators.required],
      vehicleId: [this.contract?.vehicle?.id, Validators.required]
    });

    this.filterCustomers();
    this.filterVehicles();
  }

  get customerIDControl(): FormControl {
    return this.form.get('customerId') as FormControl;
  }

  get vehicleIDControl(): FormControl {
    return this.form.get('vehicleId') as FormControl;
  }

  save(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  close() : void {
    this.dialogRef.close();
  }

  editCustomer(): void {
    this.customerIDControl.reset();
    this.selectedCustomer = undefined;
    this.filterCustomers();
  }

  onCustomerSelected(event: MatAutocompleteSelectedEvent) {
    this.customerIDControl.setValue(event.option.value['id']);
    this.selectedCustomer = event.option.value;
  }

  filterCustomers(query?: any): void {
    this.contractService.filterCustomersByLastName(query).subscribe(customers => {
      this.filteredCustomers = customers;
    });
  }

  editVehicle(): void {
    this.vehicleIDControl.reset();
    this.selectedVehicle = undefined;
    this.filterVehicles();
  }

  cancelVehicleChanges(): void {
    this.vehicleIDControl.setValue(this.contract?.vehicle?.id);
    this.selectedVehicle = this.contract?.vehicle;
    this.filterVehicles();
  }

  filterVehicles(query?: any): void {
    if (typeof query !== 'object') {
      this.contractService.filterVehiclesByBrand(query).subscribe(vehicles => {
        this.filteredVehicles = vehicles;
      });
    }
  }

  onVehicleSelected(event: MatAutocompleteSelectedEvent) {
    this.vehicleIDControl.setValue(event.option.value['id']);
    this.selectedVehicle = event.option.value;
  }

}
