CREATE TABLE user_permissions (
    user_id BIGINT NOT NULL,
    permission VARCHAR(80) NOT NULL,
    PRIMARY KEY (user_id, permission),
    CONSTRAINT fk_user_permissions_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO user_permissions (user_id, permission)
SELECT u.id, p.permission
FROM users u
CROSS JOIN (VALUES
    ('DASHBOARD_VIEW'),
    ('LICENSES_VIEW'), ('LICENSES_CREATE'), ('LICENSES_UPDATE'), ('LICENSES_DELETE'),
    ('COMPANIES_VIEW'), ('COMPANIES_CREATE'), ('COMPANIES_UPDATE'), ('COMPANIES_DELETE'),
    ('BRANCHES_VIEW'), ('BRANCHES_CREATE'), ('BRANCHES_UPDATE'), ('BRANCHES_DELETE'),
    ('USERS_VIEW'), ('USERS_CREATE'), ('USERS_UPDATE'), ('USERS_DELETE')
) AS p(permission)
WHERE u.role = 'ADMIN';

INSERT INTO user_permissions (user_id, permission)
SELECT u.id, p.permission
FROM users u
CROSS JOIN (VALUES
    ('DASHBOARD_VIEW'),
    ('LICENSES_VIEW'), ('LICENSES_CREATE'), ('LICENSES_UPDATE'),
    ('COMPANIES_VIEW'), ('COMPANIES_CREATE'), ('COMPANIES_UPDATE'),
    ('BRANCHES_VIEW'), ('BRANCHES_CREATE'), ('BRANCHES_UPDATE')
) AS p(permission)
WHERE u.role = 'USER';
