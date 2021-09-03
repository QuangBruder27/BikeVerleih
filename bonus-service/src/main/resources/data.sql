CREATE TABLE `bikeverleih`.`bonus` (
                                       `custom_id` VARCHAR(45) NOT NULL,
                                       `score` DOUBLE NOT NULL,
                                       PRIMARY KEY (`custom_id`));

INSERT INTO `bikeverleih`.`bonus`
(
    `custom_id`,
    `score`)
VALUES
(
    "kd2",
    "123.5");


UPDATE `bikeverleih`.`bonus`
SET score = '0.0'
WHERE custom_id = 'kd2';