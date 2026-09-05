CREATE TABLE license_documents (
    id BIGSERIAL PRIMARY KEY,
    license_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(120) NOT NULL,
    size BIGINT NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_license_documents_license
        FOREIGN KEY (license_id) REFERENCES licenses(id) ON DELETE CASCADE
);

CREATE INDEX ix_license_documents_license_id ON license_documents(license_id);

INSERT INTO user_permissions (user_id, permission)
SELECT u.id, p.permission
FROM users u
CROSS JOIN (VALUES
    ('LICENSE_DOCUMENT_VIEW'),
    ('LICENSE_DOCUMENT_UPLOAD'),
    ('LICENSE_DOCUMENT_DELETE')
) AS p(permission)
WHERE u.role = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_permissions (user_id, permission)
SELECT u.id, p.permission
FROM users u
CROSS JOIN (VALUES
    ('LICENSE_DOCUMENT_VIEW'),
    ('LICENSE_DOCUMENT_UPLOAD')
) AS p(permission)
WHERE u.role = 'USER'
ON CONFLICT DO NOTHING;
