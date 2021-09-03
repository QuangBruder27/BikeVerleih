CREATE TABLE `bikeverleih`.`location` (
                                          `id` INT NOT NULL AUTO_INCREMENT,
                                          `bike_id` VARCHAR(45) NOT NULL,
                                          `latitude` VARCHAR(20) NOT NULL,
                                          `longtitude` VARCHAR(20) NOT NULL,
                                          `time_created` DATETIME NOT NULL,
                                          PRIMARY KEY (`id`));


INSERT INTO `bikeverleih`.`location`
(
    `bike_id`,
    `latitude`,
    `longtitude`,
    `time_created`)
VALUES
(
    "B001",
    "52.50844515668786",
    "13.481563149994146",
    "2012-06-18 10:34:09");
