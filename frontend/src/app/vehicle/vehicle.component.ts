import {AfterViewInit, Component} from '@angular/core';
import {VehicleService} from "./service/vehicle.service";
import {MatDialog} from "@angular/material/dialog";
import {Vehicle} from "./models/vehicle.model";
import {VehicleDialogComponent} from "./dialogs/vehicle-dialog/vehicle-dialog.component";
import {NewVehicle} from "./models/new-vehicle.model";

@Component({
  selector: 'app-vehicle',
  templateUrl: './vehicle.component.html',
  styleUrls: ['./vehicle.component.scss']
})
export class VehicleComponent implements AfterViewInit {

  vehicles!: Vehicle[];
  totalResults!: number;
  pageNumber: number = 0;
  displayedColumns: string[] = ['brand', 'model', 'modelYear', 'vin', 'price', 'actions'];
  loading: boolean = true;

  constructor(private vehicleService: VehicleService,
              private dialog: MatDialog) { }

  ngAfterViewInit(): void {
    this.loadData();
  }

  nextPage(event: any) {
    this.pageNumber = event.pageIndex;
  }

  loadData(): void {
    this.loading = true;
    this.vehicleService.getAllVehicles(this.pageNumber).subscribe(vehicles => {
      this.vehicles = vehicles.vehicles;
      this.totalResults = vehicles.totalResults;
      this.loading = false;
    });
  }

  get hasVehicles(): boolean {
    return this.totalResults > 0;
  }

  openVehicleDialog(vehicle?: Vehicle, isEdit: boolean = false): void {
    const dialogRef = this.dialog.open(VehicleDialogComponent, {
      width: '500px',
      data: {vehicle:  vehicle},
      autoFocus: !isEdit
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!!result) {
        const newVehicle: NewVehicle = result;
        if (isEdit && !!vehicle) {
          this.vehicleService.updateVehicle(vehicle.id, newVehicle).subscribe(() => this.loadData());
        } else {
          this.vehicleService.saveVehicle(newVehicle).subscribe(() => this.loadData());
        }
      }
    });
  }

}
