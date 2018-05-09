DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nick VARCHAR(128) not null unique,
  password VARCHAR(128) not null,
  dni INT not null unique,
  email VARCHAR(128) not null,
  name VARCHAR(128),
  surname VARCHAR(128),
  country_id INT,
  fixture_id INT,
  created_at DATETIME,
  updated_at DATETIME,
  CHECK (dni> 0)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS matchPredictions;
CREATE TABLE IF NOT EXISTS matchPredictions(
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  prediction ENUM ('visit','tie','local') not null,
  score INT,
  created_at DATETIME,
  updated_at DATETIME,
  CHECK (score >= 0)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS matchs;
CREATE TABLE IF NOT EXISTS matchs(
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  day DATE not null,
  result ENUM ('visit','tie','local','suspended'),
  schedule INT,
  fixture_id INT,
  local_team_id INT,
  visit_team_id INT,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

DROP TABLE IF EXISTS countrys;
CREATE TABLE IF NOT EXISTS countrys(
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) not null,
  created_at DATETIME,
  updated_at DATETIME,
  UNIQUE (name)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS fixtures;
CREATE TABLE IF NOT EXISTS fixtures(
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  league VARCHAR(128) not null,
  user_id INT,
  created_at DATETIME,
  updated_at DATETIME,
  UNIQUE (league)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS matchPredictions;
CREATE TABLE IF NOT EXISTS matchPredictions(
  id INT AUTO_INCREMENT PRIMARY KEY,
  prediction ENUM ('visit','tie','local'),
  score INT,
  scheduleScore_id INT,
  match_id INT,
  user_id INT,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

DROP TABLE IF EXISTS teams; 
CREATE TABLE IF NOT EXISTS teams(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME
  UNIQUE (name)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS scheduleScores;
CREATE TABLE IF NOT EXISTS scheduleScores(
  id INT AUTO_INCREMENT PRIMARY KEY,
  score INT DEFAULT 0,
  user_id INT,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;