CREATE TABLE matchPredictions(
  id int(11) auto_increment PRIMARY KEY,
  prediction VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;