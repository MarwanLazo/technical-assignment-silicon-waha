
CREATE TABLE  IF NOT EXISTS `loguser` (
  `email` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `mobile` VARCHAR(45) NULL,
  PRIMARY KEY (`email`)
  );
  
 
  
  CREATE TABLE IF NOT EXISTS  `usertimesheet` (
   `id`  INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `login_time` TIMESTAMP  NOT NULL,
  `logout_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 -- CONSTRAINT `fk_username`
    FOREIGN KEY (`email`)
    REFERENCES `loguser` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ;
    



