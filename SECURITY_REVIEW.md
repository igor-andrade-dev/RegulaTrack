# RegulaTrack security changes and deployment checklist

## Implemented in this update

- Replaced the fixed JWT signing key with an injected, validated secret. All profiles require an externally supplied `JWT_SECRET` and reject values shorter than 32 bytes; no fixed fallback key is shipped.
- Reduced JWT lifetime to eight hours and added a per-user token version. Password reset or an administrator-initiated password change increments this version, invalidating previously issued access tokens for that user.
- Increased BCrypt work factor to 12.
- Validated login payloads and aligned password-reset rules to 12–128 characters.
- Passwords set during user creation/edit or password recovery must contain 12–128 characters. Password-reset tokens are random, stored only as SHA-256 hashes, expire in 30 minutes, can be used once, and invalidate other outstanding reset links after a successful reset.
- Scoped reset-token queries to the relevant user instead of loading the complete token table.
- Kept reset responses generic so the endpoint does not intentionally reveal whether an email is registered. Reset URLs and tokens are not written to logs.
- Sanitized unexpected API errors, mapped authentication errors to HTTP 401, removed duplicate global exception advice, restricted CORS to one configured origin, and enabled basic security headers.
- Changed default seed configuration to off, removed hard-coded demo passwords, and require explicit local seed passwords. Local Docker services bind to loopback to reduce accidental LAN exposure.
- Improved password-recovery feedback, accessible form labels/statuses, show/hide controls, and dashboard quick actions that respect user permissions.

## Required before production deployment

1. Set a unique random `JWT_SECRET` (at least 32 bytes) in the host's secret manager/environment. There is no built-in fallback key.
2. Use `SPRING_PROFILES_ACTIVE=prod`, set `APP_SEED_ENABLED=false`, and never expose PostgreSQL or Mailpit publicly.
3. Configure SMTP and `APP_MAIL_ENABLED=true`; otherwise recovery requests intentionally do not disclose links through application logs and email recovery will not reach users.
4. Set the exact HTTPS origins for `APP_FRONTEND_URL` and `APP_FRONTEND_BASE_URL`.
5. Apply the example rate limits in `deploy/nginx/` (or equivalent API Gateway/AWS WAF policies) to login and all password-reset endpoints. Review source-IP handling if a trusted load balancer/CDN sits in front of Nginx. The application CORS policy does not prevent brute force.
6. Terminate HTTPS correctly at the proxy, keep security patches current, back up the database, and rotate credentials if they were ever committed to source control.
7. After deploying the token-version migration, existing JWT sessions without the new claim will need to sign in again.

## Validation notes

Build/test execution should be performed in an environment that can download the Maven wrapper distribution and npm dependencies. In the current environment, Maven dependency bootstrap did not complete because the configured repository could not be reached; that is an environment limitation, not a passing build result.
