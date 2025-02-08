CREATE TABLE `products`
(
    `ID`   INT PRIMARY KEY,
    `name` varchar(20),
    `type` varchar(20)
);

CREATE TABLE `contracts`
(
    `ID` INT PRIMARY KEY,
    `product` INT,
    `revenue` DECIMAL,
    `date_signed` date
);

CREATE TABLE `revenue_recognition`
(
    `contract` INT,
    `amount` DECIMAL,
    `recognition_on` date,
    PRIMARY KEY (contract, recognition_on)
);
