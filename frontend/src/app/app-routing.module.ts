import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import {DashboardComponent} from "./dashboard/dashboard.component";

const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'contracts', loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule) },
  { path: 'vehicles', loadChildren: () => import('./vehicle/vehicle.module').then(m => m.VehicleModule) },
  { path: 'customers', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  { path: '**', redirectTo: '/dashboard', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
