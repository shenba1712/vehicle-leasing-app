import {BrandModel} from "./brand.model";

// Basic helper class to populate some brands and models.
export class BrandsUtil {
  static MERCEDES: BrandModel = {
      brand: 'Mercedes',
      models: ['S-Class', 'SLS AMG', 'Mercedes-AMG GT', 'G-Class', 'GLC-Class', 'EQC', 'SL-Class', 'Mercedes-AMG C 63 S', 'A-Class Sedan', 'Mercedes-AMG GT 63 S']
  };
  static BMW: BrandModel = {
      brand: 'BMW',
      models: ['X1', 'X3', 'iX', 'i3', 'M8 Coupe', '3 Series Touring', 'BMW 8 Convertible']
  };
  static VOLVO: BrandModel = {
      brand: 'Volvo',
      models: ['XC90', 'V90', 'S90', 'XC40']
  };
  static VOLKSWAGEN: BrandModel = {
      brand: 'VolksWagen',
      models: ['Golf', 'T-Cross', 'Tiguan', 'Caddy', 'Polo']
  };
  static AUDI: BrandModel = {
      brand: 'Audi',
      models: ['A3', 'Q3', 'Q5']
  }
  static brandModels = [BrandsUtil.MERCEDES, BrandsUtil.BMW, BrandsUtil.VOLVO, BrandsUtil.VOLKSWAGEN, BrandsUtil.AUDI];

  static populateBrands(): string[] {
    return BrandsUtil.brandModels.map(brandModel => brandModel.brand);
  }

  static populateModels(brand?: string): string[] {
    const brandModel = BrandsUtil.brandModels.find(brandModel => brandModel.brand === brand);
    return !!brandModel ? brandModel.models: [];
  }
}
