CREATE TABLE `bikeverleih`.`custom` (
                                        `custom_id` INT NOT NULL AUTO_INCREMENT,
                                        `email` VARCHAR(45) NOT NULL,
                                        `name` VARCHAR(20) NOT NULL,
                                        `password` VARCHAR(12) NOT NULL,
                                        PRIMARY KEY (`custom_id`),
                                        UNIQUE INDEX `email_UNIQUE` (`email` ASC));


INSERT INTO `bikeverleih`.`custom`
(
    `email`,
    `name`,
    `password`)
VALUES
(
    "email1@gmail.com",
    "Quang1",
    "123456");

CREATE TABLE `bikeverleih`.`bike_auth` (
                                           `bike_id` VARCHAR(20) NOT NULL,
                                           `password` VARCHAR(20) NOT NULL,
                                           PRIMARY KEY (`bike_id`));
