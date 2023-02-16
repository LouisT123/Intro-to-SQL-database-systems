CREATE TABLE InsuranceCo (name VARCHAR(50) PRIMARY KEY, phone INT);
CREATE TABLE Vehicle (licensePlate VARCHAR(50) PRIMARY KEY, year INT, maxLiability REAL, insuranceName VARCHAR(50) REFERENCES InsuranceCo(name), personSsn INT REFERENCES Person(ssn));
CREATE TABLE Car (licensePlate VARCHAR(50) PRIMARY KEY REFERENCES Vehicle(licensePlate), make VARCHAR(50));
CREATE TABLE Truck (licensePlate VARCHAR(50) PRIMARY KEY REFERENCES Vehicle(licensePlate), capacity INT, truckSsn INT REFERENCES ProfessionalDriver(ssn));
CREATE TABLE NonProfessionalDriver (ssn INT PRIMARY KEY REFERENCES Driver(ssn));
CREATE TABLE ProfessionalDriver (ssn INT PRIMARY KEY REFERENCES Driver(ssn), medicalhistory VARCHAR(200));
CREATE TABLE Person (ssn INT PRIMARY KEY, name VARCHAR(50));
CREATE TABLE Driver (ssn INT PRIMARY KEY REFERENCES Person(ssn), name VARCHAR(50) REFERENCES Person(name), driverID INT);

/* B. The vehicle table represents the relationship insures since it is a many to 1 realtionship directed at the InsuranceCo table. This is done with a foreign reference to represent that many vehicles can have only 1 insurance company but multiple insurance companies cannot "have" 1 car. */
/* C. drives is a many to many relationship while operates is a many to one relationship, operates requires the foreign key reference Proffesional Driver, making it more restrictive to operate a truck then drive a vehicle*/