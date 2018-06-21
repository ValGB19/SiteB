ALTER TABLE users 
	ADD admin boolean default false,
	MODIFY COLUMN email VARCHAR(128) not null unique;
