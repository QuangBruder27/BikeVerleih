CREATE TABLE `bikeverleih`.`customer` (
                                        `customer_id` VARCHAR(12) NOT NULL,
                                        `email` VARCHAR(45) NOT NULL,
                                        `name` VARCHAR(20) NOT NULL,
                                        `password` VARCHAR(256) NOT NULL,
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
    "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b");

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

INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B004","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B005","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B006","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B007","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B008","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B009","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B010","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B011","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B012","1357");
INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B013","1357");

INSERT INTO `bikeverleih`.`bike_auth`(`bike_id`,`password`) VALUES("B666","1");