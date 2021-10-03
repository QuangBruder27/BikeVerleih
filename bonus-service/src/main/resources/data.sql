CREATE TABLE `bikeverleih`.`bonus` (
                                       `customer_id` VARCHAR(12) NOT NULL,
                                       `score` INT NOT NULL,
                                       PRIMARY KEY (`customer_id`));

INSERT INTO `bikeverleih`.`bonus`
(
    `customer_id`,
    `score`)
VALUES
(
    "KD0001",
    "123");

INSERT INTO `bikeverleih`.`bonus`
(
    `customer_id`,
    `score`)
VALUES
(
    "KD0002",
    "0");



UPDATE `bikeverleih`.`bonus`
SET score = '0.0'
WHERE customer_id = 'kd2';