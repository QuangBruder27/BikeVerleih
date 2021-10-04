CREATE TABLE `bikeverleih`.`bike_rent` (
                                      `bike_id` VARCHAR(20) NOT NULL,
                                      `status` ENUM('available', 'reserved', 'damaged') NOT NULL,
                                      `pin` INT NULL,
                                      `latitude` VARCHAR(20) NOT NULL,
                                      `longtitude` VARCHAR(20) NOT NULL,
                                      PRIMARY KEY (`bike_id`));

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B001","available","52.51357943161985","13.482541018219138");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`pin`,`latitude`,`longtitude`)
VALUES ("B002","reserved",1111, "52.50154157556343","13.478155468056443");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B003","available","52.50154157556343","13.478155468056492");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B004","available", "52.5127799524728","13.48750826570198");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B005","available", "52.39168910846257","13.066651390847639");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B006","available", "52.5036380749826"," 13.485596599805215");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B007","available", "52.52529309116041","13.464276445938257");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B008","available", "52.511259348060264","13.43549680055044");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B009","available", "52.45251458673761","13.537500846336275");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B010","available", "52.455545888660446","13.51356241871937");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B011","available", "52.45247442613484","13.538469816920786");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B012","available", "52.456756999829864","13.525685037595656");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B013","available", "52.456756999829865","13.525685037595656");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B014","available", "52.4708699629399","13.517199520815273");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B015","available", "52.45928260648818","13.526448653649133");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B016","available", "52.504840083659204", "13.484995515836722");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B017","available", "52.50112968726727","13.49623822102576");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B018","available", "52.51351291681828", "13.48216341414339");

INSERT INTO `bikeverleih`.`bike_rent`(`bike_id`, `status`,`latitude`,`longtitude`)
VALUES ("B666","available", "52.54529240113297", "13.351692379102936");

CREATE TABLE `bikeverleih`.`booking` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `customer_id` VARCHAR(12) NOT NULL,
                                       `bike_id` VARCHAR(20) NOT NULL,
                                       `begin_time` DATETIME NULL,
                                       `end_time` DATETIME NULL,
                                       `distance` INT NULL,
                                       `status` ENUM('reserved', 'running', 'completed')  NULL,
                                       PRIMARY KEY (`id`));

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`)
VALUES ("KD0003","B002","reserved","2021-09-20 23:30:09");

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`)
VALUES ("KD0002","B001","reserved","2021-09-29 23:30:09");

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`)
VALUES ("Tester","B018","reserved","2021-09-29 23:30:09");

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`,`end_time`, `distance`)
VALUES ("KD0001","B003","completed","2021-09-07 23:30:09","2021-09-07 23:34:09","5.0");

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`,`end_time`, `distance`)
VALUES ("KD0001","B004","completed","2021-09-09 14:30:09","2021-09-09 13:34:09","2.0");

INSERT INTO `bikeverleih`.`booking`(`customer_id`,`bike_id`,`status`,`begin_time`,`end_time`, `distance`)
VALUES ("KD0001","B004","completed","2021-09-06 09:30:09","2021-09-06 10:34:09","10.0");