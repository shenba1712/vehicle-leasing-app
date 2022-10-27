import {AfterViewInit, Component} from '@angular/core';
import {Customer} from "./models/customer.model";
import {CustomerService} from "./service/customer.service";
import {MatDialog} from "@angular/material/dialog";
import {CustomerDialogComponent} from "./dialogs/customer-dialog/customer-dialog.component";
import {NewCustomer} from "./models/new-customer.model";

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements AfterViewInit {

  // load initial data
  customers!: Customer[];
  totalResults!: number;
  pageNumber: number = 0;
  displayedColumns: string[] = ['firstName', 'lastName', 'birthDate', 'actions'];
  loading: boolean = true;

  constructor(private customerService: CustomerService,
              private dialog: MatDialog) { }

  ngAfterViewInit(): void {
    this.loadData();
  }

  nextPage(event: any) {
    this.pageNumber = event.pageIndex;
  }

  loadData(): void {
    this.loading = true;
    this.customerService.getAllCustomers(this.pageNumber).subscribe(customers => {
      this.customers = customers.customers;
      this.totalResults = customers.totalResults;
      this.loading = false;
    });
  }

  get hasCustomers(): boolean {
    return this.totalResults > 0;
  }

  openCustomerDialog(customer?: Customer, isEdit: boolean = false): void {
    const dialogRef = this.dialog.open(CustomerDialogComponent, {
      width: '500px',
      data: {customer:  customer},
      autoFocus: !isEdit
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!!result) {
        const newCustomer: NewCustomer = result;
        if (isEdit && !!customer) {
          this.customerService.updateCustomer(customer.id, newCustomer).subscribe(() => this.loadData());
        } else {
          this.customerService.saveCustomer(newCustomer).subscribe(() => this.loadData());
        }
      }
    });
  }

}
