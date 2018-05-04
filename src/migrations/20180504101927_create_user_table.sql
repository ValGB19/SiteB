CREATE TABLE users (
  username VARCHAR(128) PRIMARY KEY,
  password VARCHAR(128),
  email VARCHAR(128),
  name VARCHAR(128),
  surname VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;