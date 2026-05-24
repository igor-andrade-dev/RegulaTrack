CREATE TABLE licenses (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          category VARCHAR(100),
                          issuer VARCHAR(150),
                          expires_at DATE NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          responsible_name VARCHAR(150),
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);