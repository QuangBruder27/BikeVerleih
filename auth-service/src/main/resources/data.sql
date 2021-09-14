CREATE TABLE `bikeverleih`.`customer` (
                                        `customer_id` VARCHAR(12) NOT NULL,
                                        `email` VARCHAR(45) NOT NULL,
                                        `name` VARCHAR(20) NOT NULL,
                                        `password` VARCHAR(12) NOT NULL,
                                        PRIMARY KEY (`customer_id`),
                                        UNIQUE INDEX `email_UNIQUE` (`email` ASC));

INSERT INTO `bikeverleih`.`customer`
(
    `customer_id`,
    `email`,
    `name`,
    `password`)
VALUES
(
    "KD0001",
    "email1@gmail.com",
    "Quang1",
    "1");

INSERT INTO `bikeverleih`.`customer`
(
    `customer_id`,
    `email`,
    `name`,
    `password`)
VALUES
(
    "KD0002",
    "email2@gmail.com",
    "Quang2",
    "654321");



CREATE TABLE `bikeverleih`.`bike_auth` (
                                           `bike_id` VARCHAR(20) NOT NULL,
                                           `password` VARCHAR(20) NOT NULL,
                                           PRIMARY KEY (`bike_id`));
INSERT INTO `bikeverleih`.`bike_auth`
(
    `bike_id`,
    `password`)
VALUES
(
    "B001",
    "1357");
INSERT INTO `bikeverleih`.`bike_auth`
(
    `bike_id`,
    `password`)
VALUES
(
    "B002",
    "1357");
INSERT INTO `bikeverleih`.`bike_auth`
(
    `bike_id`,
    `password`)
VALUES
(
    "B003",
    "1357");
INSERT INTO `bikeverleih`.`bike_auth`
(
    `bike_id`,
    `password`)
VALUES
(
    "B004",
    "1357");