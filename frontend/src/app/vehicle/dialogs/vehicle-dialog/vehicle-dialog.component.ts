import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../models/vehicle.model";
import {BrandsUtil} from "../../models/brands.util";

@Component({
  selector: 'app-vehicle-dialog',
  templateUrl: './vehicle-dialog.component.html',
  styleUrls: ['./vehicle-dialog.component.scss']
})
export class VehicleDialogComponent implements OnInit {

  vehicle: Vehicle;
  form!: FormGroup;
  brands: string[] = BrandsUtil.populateBrands();
  models: string[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<VehicleDialogComponent>,
              private formBuilder: FormBuilder) {
    this.vehicle = data['vehicle'];
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      brand: [this.vehicle?.brand, Validators.required],
      model: [this.vehicle?.model, Validators.required],
      modelYear: [this.vehicle?.modelYear, Validators.required],
      vin: [this.vehicle?.vin],
      price: [this.vehicle?.price, Validators.required]
    });
    if (!!this.vehicle) {
      this.loadModels(false);
    }
  }

  get brand(): FormControl {
    return this.form?.get('brand') as FormControl;
  }

  get model(): FormControl {
    return this.form?.get('model') as FormControl;
  }

  loadModels(newBrand: boolean = true): void {
    if (newBrand) {
      this.model.reset();
    }
    this.models = BrandsUtil.populateModels(this.brand.value);
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
