import {AfterViewInit, Component} from '@angular/core';
import {Contract} from "./models/contract.model";
import {MatDialog} from "@angular/material/dialog";
import {ContractService} from "./service/contract.service";
import {NewContract} from "./models/new-contract.model";
import {ContractDialogComponent} from "./dialogs/contract-dialog/contract-dialog.component";
import {ContractDetailsDialogComponent} from "./dialogs/contract-details-dialog/contract-details-dialog.component";

@Component({
  selector: 'app-contract',
  templateUrl: './contract.component.html',
  styleUrls: ['./contract.component.scss']
})
export class ContractComponent implements AfterViewInit {

  contracts!: Contract[];
  totalResults!: number;
  pageNumber: number = 0;
  displayedColumns: string[] = ['contractNumber', 'customer', 'vehicle', 'vin', 'monthlyRate', 'price', 'actions'];
  loading: boolean = true;

  constructor(private contractService: ContractService,
              private dialog: MatDialog) { }

  ngAfterViewInit(): void {
    this.loadData();
  }

  nextPage(event: any) {
    this.pageNumber = event.pageIndex;
  }

  loadData(): void {
    this.loading = true;
    this.contractService.getAllContracts(this.pageNumber).subscribe(contracts => {
      this.contracts = contracts.contracts;
      this.totalResults = contracts.totalResults;
      this.loading = false;
    });
  }

  get hasContracts(): boolean {
    return this.totalResults > 0;
  }

  openContractDialog(contract?: Contract, isEdit: boolean = false): void {
    const dialogRef = this.dialog.open(ContractDialogComponent, {
      width: '500px',
      data: {contract:  contract, isEdit: isEdit},
      autoFocus: !isEdit
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!!result) {
        const newContract: NewContract = result;
        if (isEdit && !!contract) {
          this.contractService.updateContract(contract.contractNumber, newContract).subscribe(() => this.loadData());
        } else {
          this.contractService.saveContract(newContract).subscribe(() => this.loadData());
        }
      }
    });
  }

  openContractDetailsDialog(contract?: Contract): void {
    const dialogRef = this.dialog.open(ContractDetailsDialogComponent, {
      width: '500px',
      data: {contract:  contract},
      autoFocus: false
    });
    dialogRef.afterClosed().subscribe();
  }

}
