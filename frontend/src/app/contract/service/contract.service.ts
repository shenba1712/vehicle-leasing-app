import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BackendRoutesConfig} from "../../backend-routes.config";
import {Observable} from "rxjs";
import {LeasingContracts} from "../models/leasing-contracts.model";
import {NewContract} from "../models/new-contract.model";
import {LeasingCustomer} from "../models/leasing-customer.model";
import {LeasingVehicle} from "../models/leasing-vehicle.model";

@Injectable()
export class ContractService {
  private readonly baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = BackendRoutesConfig.baseUrl + BackendRoutesConfig.contract.baseUrl;
  }

  public getAllContracts(pageNumber: number): Observable<LeasingContracts> {
    let url = this.baseUrl + BackendRoutesConfig.contract.allContracts
      .replace('{pageNumber}', String(pageNumber));
    return this.http.get<LeasingContracts>(url);
  }

  public saveContract(contract: NewContract): Observable<any> {
    return this.http.post<any>(this.baseUrl, contract);
  }

  public updateContract(id: number | undefined, contract: NewContract): Observable<any> {
    let url = this.baseUrl + BackendRoutesConfig.contract.individual
      .replace('{id}', String(id));
    return this.http.put<any>(url, contract);
  }

  public filterCustomersByLastName(query?: string): Observable<LeasingCustomer[]> {
      let url = this.baseUrl + BackendRoutesConfig.contract.filterCustomers;
      if (typeof query === 'string' && !!query?.trim()) {
        url = url + '?query=' + query;
      }
      return this.http.get<any>(url);
  }

  public filterVehiclesByBrand(brandQuery?: string): Observable<LeasingVehicle[]> {
    let url = this.baseUrl + BackendRoutesConfig.contract.filterVehicles;
    if (typeof brandQuery == 'string' && !!brandQuery.trim()) {
      url = url + '?brandQuery=' + brandQuery;
    }
    return this.http.get<any>(url);
  }
}
