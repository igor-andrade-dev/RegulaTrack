CREATE TABLE licenses (
                          id BIGSERIAL PRIMARY KEY,

                          company_id BIGINT NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          license_number VARCHAR(100),
                          issuing_agency VARCHAR(150),

                          issue_date DATE,
                          expiration_date DATE,

                          status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_licenses_company
                              FOREIGN KEY (company_id)
                                  REFERENCES companies(id)
                                  ON DELETE CASCADE
);