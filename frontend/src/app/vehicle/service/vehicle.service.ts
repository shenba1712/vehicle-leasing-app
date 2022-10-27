import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BackendRoutesConfig} from "../../backend-routes.config";
import {Observable} from "rxjs";
import {Vehicles} from "../models/vehicles.model";
import {NewVehicle} from "../models/new-vehicle.model";
import {Vehicle} from "../models/vehicle.model";

@Injectable({
  providedIn: "root"
})
export class VehicleService {
  private readonly baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = BackendRoutesConfig.baseUrl + BackendRoutesConfig.vehicle.baseUrl;
  }

  public getAllVehicles(pageNumber: number): Observable<Vehicles> {
    let url = this.baseUrl + BackendRoutesConfig.vehicle.allVehicles
      .replace('{pageNumber}', String(pageNumber));
    return this.http.get<Vehicles>(url);
  }

  public saveVehicle(vehicle: NewVehicle): Observable<any> {
    return this.http.post<any>(this.baseUrl, vehicle);
  }

  public updateVehicle(id: number | undefined, vehicle: NewVehicle): Observable<any> {
    let url = this.baseUrl + BackendRoutesConfig.vehicle.individual
      .replace('{id}', String(id));
    return this.http.put<any>(url, vehicle);
  }

  public getVehicle(id: number): Observable<Vehicle> {
    let url = this.baseUrl + BackendRoutesConfig.vehicle.individual
      .replace('{id}', String(id));
    return this.http.get<Vehicle>(url);
  }
}
