CREATE TABLE licenses (
                          id BIGSERIAL PRIMARY KEY,

                          company_id BIGINT NOT NULL,
                          branch_id BIGINT NOT NULL,

                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          category VARCHAR(100),

                          license_number VARCHAR(100),
                          issuer VARCHAR(150),

                          issued_at DATE,
                          expires_at DATE,

                          responsible_name VARCHAR(150),
                          responsible_email VARCHAR(150),

                          status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                          notes TEXT,

                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_licenses_company
                              FOREIGN KEY (company_id)
                                  REFERENCES companies(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_licenses_branch
                              FOREIGN KEY (branch_id)
                                  REFERENCES branches(id)
                                  ON DELETE CASCADE
);