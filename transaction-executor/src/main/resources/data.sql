CREATE TABLE IF NOT EXISTS accounts
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(255) UNIQUE NOT NULL,
    balance        DECIMAL(15, 2)
);

INSERT INTO accounts (account_number, balance)
VALUES ('123456789012345678', 1000.00);
INSERT INTO accounts (account_number, balance)
VALUES ('123456789012345679', 500.00);
INSERT INTO accounts (account_number, balance)
VALUES ('123456789012345676', 50.00);