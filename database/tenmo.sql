BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
	increment by 1
	start with 3001
	no maxvalue;
	
CREATE TABLE transfer(
	transfer_id int not null default nextval('seq_transfer_id'),
	sender int not null, 
	receiver int not null,
	amount numeric (13,2) not null,
	transfer_date date not null,
	transfer_type varchar (10) NOT NULL,
	status varchar (10) not null,
	constraint PK_transfer primary key (transfer_id),
	constraint FK_transfer_receiver foreign key (receiver) references account (account_id),
	constraint FK_transfer_sender foreign key (sender) references account (account_id),
	CONSTRAINT CHK_type CHECK (transfer_type IS NOT NULL OR transfer_type IN('Send', 'Request')),
	constraint CHK_status check (status is not null or status in ('Approved', 'Pending', 'Denied'))
	);
COMMIT;
