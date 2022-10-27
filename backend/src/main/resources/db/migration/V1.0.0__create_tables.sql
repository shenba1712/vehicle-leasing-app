CREATE TABLE IF NOT EXISTS `leasingContract` (
    contractNumber INT NOT NULL AUTO_INCREMENT,
    monthlyRate DECIMAL,
    primary key (contractNumber)
);

CREATE TABLE IF NOT EXISTS `customer` (
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    birthDate DATE,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS `vehicle` (
    id INT NOT NULL AUTO_INCREMENT,
    brand VARCHAR(50),
    model VARCHAR(50),
    modelYear YEAR,
    VIN VARCHAR(25),
    price DECIMAL,
    primary key (id)
);
