export class Vehicle {
  id?: number;
  brand: string = '';
  model: string = '';
  vin: string = '-'
  price: number = 0.0;
  modelYear: number = new Date().getUTCFullYear(); // set default year as current year
}
