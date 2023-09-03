INSERT INTO tb_user (email, password) VALUES ('admin@gmail.com', '$2a$10$uLIGjR0odP2/OquNISCuaeQVMCKttEr7FkCKSjE1l8KTQp/WhmbxC');
INSERT INTO tb_user (email, password) VALUES ('worker@gmail.com', '$2a$10$zByzWndsZCulPmyanUpnceXI7ERpn2//PFSVC8iJLEW.0kvHuF7X6');
INSERT INTO tb_user (email, password) VALUES ('user@gmail.com', '$2a$10$gR/4HNBmU0Mn/FW5kAHGIubS9.4j5ufopBREthylDsOgzR7PgA0Aa');

INSERT INTO roles (authority) VALUES ('ROLE_ADMIN');
INSERT INTO roles (authority) VALUES ('ROLE_WORKER');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO clients (name, email) VALUES ('client1', 'client1@gmail.com');
INSERT INTO clients (name, email) VALUES ('client2', 'client2@gmail.com');
INSERT INTO clients (name, email) VALUES ('client3', 'client3@gmail.com');
INSERT INTO clients (name, email) VALUES ('client4', 'client4@gmail.com');
INSERT INTO clients (name, email) VALUES ('client5', 'client5@gmail.com');