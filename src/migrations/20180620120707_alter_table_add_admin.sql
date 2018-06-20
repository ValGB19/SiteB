ALTER TABLE users 
	ADD admin boolean,
	MODIFY COLUMN email VARCHAR(128) not null unique;
