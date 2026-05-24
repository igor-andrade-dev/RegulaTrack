CREATE TABLE companies (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(150) NOT NULL,
                           document_number VARCHAR(50),
                           segment VARCHAR(100),
                           country VARCHAR(100),
                           city VARCHAR(100),
                           created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                           updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE branches (
                          id BIGSERIAL PRIMARY KEY,
                          company_id BIGINT NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          address VARCHAR(255),
                          city VARCHAR(100),
                          state VARCHAR(100),
                          country VARCHAR(100),
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_branches_company
                              FOREIGN KEY (company_id)
                                  REFERENCES companies(id)
                                  ON DELETE CASCADE
);