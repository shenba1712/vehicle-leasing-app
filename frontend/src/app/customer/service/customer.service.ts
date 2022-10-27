import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BackendRoutesConfig} from "../../backend-routes.config";
import {Observable} from "rxjs";
import {Customers} from "../models/customers.model";
import {NewCustomer} from "../models/new-customer.model";
import {Customer} from "../models/customer.model";

@Injectable({
  providedIn: "root"
})
export class CustomerService {
  private readonly baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = BackendRoutesConfig.baseUrl + BackendRoutesConfig.customer.baseUrl;
  }

  public getAllCustomers(pageNumber: number): Observable<Customers> {
    let url = this.baseUrl + BackendRoutesConfig.customer.allCustomers
      .replace('{pageNumber}', String(pageNumber));
    return this.http.get<Customers>(url);
  }

  public saveCustomer(customer: NewCustomer): Observable<any> {
    return this.http.post<any>(this.baseUrl, customer);
  }

  public updateCustomer(id: number | undefined, customer: NewCustomer): Observable<any> {
    let url = this.baseUrl + BackendRoutesConfig.customer.individual
      .replace('{id}', String(id));
    return this.http.put<any>(url, customer);
  }

  public getCustomer(id: number): Observable<Customer> {
    let url = this.baseUrl + BackendRoutesConfig.customer.individual
      .replace('{id}', String(id));
    return this.http.get<Customer>(url);
  }
}
