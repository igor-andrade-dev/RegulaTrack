ALTER TABLE licenses
    ADD COLUMN company_id BIGINT,
ADD COLUMN branch_id BIGINT;

ALTER TABLE licenses
    ADD CONSTRAINT fk_licenses_company
        FOREIGN KEY (company_id)
            REFERENCES companies(id);

ALTER TABLE licenses
    ADD CONSTRAINT fk_licenses_branch
        FOREIGN KEY (branch_id)
            REFERENCES branches(id);