CREATE TABLE `bikeverleih`.`bike` (
                                      `bike_id` VARCHAR(45) NOT NULL,
                                      `status` ENUM('available', 'reserved', 'damaged') NOT NULL,
                                      `pin` INT NULL,
                                      PRIMARY KEY (`bike_id`));

 INSERT INTO `bikeverleih`.`bike`(`bike_id`, `status`)
 VALUES ("B001","available");

INSERT INTO `bikeverleih`.`bike`(`bike_id`, `status`,`pin`)
 VALUES ("B002","reserved",1111);

 INSERT INTO `bikeverleih`.`bike`(`bike_id`, `status`)
 VALUES ("B003","available");


CREATE TABLE `bikeverleih`.`booking` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `custom_id` VARCHAR(45) NOT NULL,
                                       `bike_id` VARCHAR(45) NOT NULL,
                                       `start_time` DATETIME NULL,
                                       `end_time` DATETIME NULL,
                                       `distance` INT NULL,
                                       `status` ENUM('reserved', 'running', 'completed') NOT NULL,
                                       PRIMARY KEY (`id`));

INSERT INTO `bikeverleih`.`booking`(`custom_id`,`bike_id`,`status`)
VALUES ("KD001","B001","reserved")