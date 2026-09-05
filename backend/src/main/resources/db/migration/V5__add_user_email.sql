ALTER TABLE users ADD COLUMN email VARCHAR(160);
CREATE UNIQUE INDEX ux_users_email ON users(email) WHERE email IS NOT NULL;
