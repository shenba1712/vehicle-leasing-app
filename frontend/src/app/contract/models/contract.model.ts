import {LeasingCustomer} from "./leasing-customer.model";
import {LeasingVehicle} from "./leasing-vehicle.model";

export class Contract {
  contractNumber!: number;
  monthlyRate!: number;
  customer!: LeasingCustomer;
  vehicle!: LeasingVehicle;
}
