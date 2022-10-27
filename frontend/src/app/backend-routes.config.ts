export const BackendRoutesConfig = {
  baseUrl: 'http://localhost:8080/',
  customer: {
    baseUrl: 'customer',
    individual: '/{id}',
    allCustomers: '/page/{pageNumber}'
  },
  vehicle: {
    baseUrl: 'vehicle',
    individual: '/{id}',
    allVehicles: '/page/{pageNumber}'
  },
  contract: {
    baseUrl: 'contract',
    allContracts: '/overview/{pageNumber}',
    individual: '/{id}',
    filterCustomers: '/filter/customers',
    filterVehicles: '/filter/vehicles'
  }
};
