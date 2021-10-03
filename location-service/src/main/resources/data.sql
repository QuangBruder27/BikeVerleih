CREATE TABLE `bikeverleih`.`bike_location` (
                                          `id` INT NOT NULL AUTO_INCREMENT,
                                          `bike_id` VARCHAR(20) NOT NULL,
                                          `latitude` VARCHAR(20) NOT NULL,
                                          `longtitude` VARCHAR(20) NOT NULL,
                                          `time_created` DATETIME NOT NULL,
                                          PRIMARY KEY (`id`));


INSERT INTO `bikeverleih`.`bike_location`
(
    `bike_id`,
    `latitude`,
    `longtitude`,
    `time_created`)
VALUES
(
    "B001",
    "52.50343552336937",
    "13.469401001991985",
    "2021-09-07 23:34:09");

INSERT INTO `bikeverleih`.`bike_location`
(
    `bike_id`,
    `latitude`,
    `longtitude`,
    `time_created`)
VALUES
(
    "B003",
    "52.50154157556343",
    "13.478155468056492",
    "2021-09-07 23:34:09");

INSERT INTO `bikeverleih`.`bike_location`
(
    `bike_id`,
    `latitude`,
    `longtitude`,
    `time_created`)
VALUES
(
    "B004",
    "52.5127799524728",
    "13.48750826570198",
    "2021-09-07 23:34:09");
