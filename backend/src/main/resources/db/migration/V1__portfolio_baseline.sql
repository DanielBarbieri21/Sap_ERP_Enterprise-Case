CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS app_release_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    installed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO app_release_history (version, description)
SELECT '1.0.0', 'Portfolio baseline managed by Flyway'
WHERE NOT EXISTS (
    SELECT 1
    FROM app_release_history
    WHERE version = '1.0.0'
);
