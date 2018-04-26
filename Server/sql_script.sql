DROP TABLE TeachedPhrases;
DROP TABLE PhraseReport;
DROP TABLE DeletedPhrases;
DROP TABLE User;

DROP DATABASE kalassist;
CREATE DATABASE kalassist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kalassist;

CREATE TABLE User (Username varchar(50) not null PRIMARY KEY,Email varchar(100),Password varchar(100));
CREATE TABLE TeachedPhrases (TPID int not null auto_increment PRIMARY KEY,
                             Creator varchar(50) not null,
                             Question varchar(100) not null,
                             Answer varchar(100) not null,
                             FOREIGN KEY (Creator) REFERENCES User(Username));


CREATE TABLE DeletedPhrases (DPID int not null auto_increment PRIMARY KEY,
                            Creator varchar(50) not null,
                            Question varchar(100) not null,
                            Answer varchar(100) not null,
                            Date DATE not null,
                            FOREIGN KEY (Creator) REFERENCES User(Username));

CREATE TABLE PhraseReport (TPID int not null,
                           Username varchar(50),
                           PRIMARY KEY (TPID,Username));

INSERT INTO User VALUES ("oemer","oemer@live.de","8f14e45fceea167a5a36dedd4bea2543");
INSERT INTO TeachedPhrases VALUES (1,"oemer","Magst du Kekse","Ich bin nicht der Cookie Monster !");

DELIMITER //
CREATE TRIGGER trig_add_report AFTER INSERT ON PhraseReport
FOR EACH ROW
BEGIN
    DECLARE reportCount INT;
    SET reportCount = (SELECT Count(*) FROM PhraseReport WHERE TPID = NEW.TPID GROUP BY TPID);

    IF (reportCount > 2)
    THEN
      DELETE FROM TeachedPhrases WHERE TPID = NEW.TPID;
    END IF;

END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER trig_del_report BEFORE DELETE ON TeachedPhrases
FOR EACH ROW
BEGIN
  INSERT INTO DeletedPhrases VALUES ('',OLD.Creator,OLD.Question,OLD.Answer,NOW());
END //
DELIMITER ;


SELECT * FROM User;
SELECT * FROM TeachedPhrases;
SELECT * FROM DeletedPhrases;
SELECT * FROM PhraseReport;
