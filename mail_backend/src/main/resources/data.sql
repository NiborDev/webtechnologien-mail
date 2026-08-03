-- ============================================================================
-- INITIAL TEST DATA FOR MAIL BACKEND (data.sql)
-- Executed on Spring Boot startup after JPA DDL creation
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. USERS
-- Password for all test users: Password123!
-- Argon2id encoded with pepper: my-pepper-secret-for-password-encoding
-- ----------------------------------------------------------------------------
INSERT INTO users (user_id, email, password, first_name, last_name, created_at, updated_at) VALUES
('00000000-0000-0000-0000-000000000001', 'test@example.com', '$argon2id$v=19$m=65536,t=6,p=1$4xjnGFK/liZW6o69ofd2EiRFH8acl5o60H7dJmqdUmo$rErBkdWOocvOr7eFP7hnYY+3nctBELoqYgB6iwZKLHo', 'Test', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('11111111-1111-1111-1111-111111111111', 'max.mustermann@example.com', '$argon2id$v=19$m=65536,t=6,p=1$4xjnGFK/liZW6o69ofd2EiRFH8acl5o60H7dJmqdUmo$rErBkdWOocvOr7eFP7hnYY+3nctBELoqYgB6iwZKLHo', 'Max', 'Mustermann', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222', 'anna.schmidt@example.com', '$argon2id$v=19$m=65536,t=6,p=1$4xjnGFK/liZW6o69ofd2EiRFH8acl5o60H7dJmqdUmo$rErBkdWOocvOr7eFP7hnYY+3nctBELoqYgB6iwZKLHo', 'Anna', 'Schmidt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333', 'erika.musterfrau@example.com', '$argon2id$v=19$m=65536,t=6,p=1$4xjnGFK/liZW6o69ofd2EiRFH8acl5o60H7dJmqdUmo$rErBkdWOocvOr7eFP7hnYY+3nctBELoqYgB6iwZKLHo', 'Erika', 'Musterfrau', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ----------------------------------------------------------------------------
-- 2. MAILS
-- Statuses: DRAFT, SENT, RECEIVED, DELETED, ARCHIVED
-- Source: 0 (INTERN), 1 (EXTERN)
-- ----------------------------------------------------------------------------
INSERT INTO mails (mail_id, from_email, subject, body_text, status, is_read, is_marked, sent_at, received_at, source) VALUES
-- Inbox Mail 1 (RECEIVED, Unread, Marked)
('a0000000-0000-0000-0000-000000000001', 'max.mustermann@example.com', 'Willkommen beim neuen Mail-Client!', 'Hallo Test-User,\n\nherzlich willkommen beim neuen Webtechnologien Mail-Client!\nDas Backend wurde erfolgreich eingerichtet und die Testdaten geladen.\n\nViele Grüße,\nMax Mustermann', 'RECEIVED', FALSE, TRUE, '2026-08-01 09:00:00', '2026-08-01 09:01:00', 0),

-- Inbox Mail 2 (RECEIVED, Read)
('a0000000-0000-0000-0000-000000000002', 'anna.schmidt@example.com', 'Projekttreffen am Donnerstag', 'Hallo,\n\nerinnerst du dich an unser geplantes Meeting am Donnerstag um 14:00 Uhr im Raum A10?\nBitte bereite die Präsentation zum Backend-Design vor.\n\nBeste Grüße,\nAnna Schmidt', 'RECEIVED', TRUE, FALSE, '2026-08-02 11:30:00', '2026-08-02 11:31:00', 0),

-- Sent Mail (SENT)
('a0000000-0000-0000-0000-000000000003', 'test@example.com', 'Zwischenstand Webtechnologien-Projekt', 'Hallo Erika,\n\nich habe die Änderungen am Backend eingepflegt. Die REST-Endpoints und Testdaten sind nun einsatzbereit.\n\nViele Grüße,\nTest User', 'SENT', TRUE, FALSE, '2026-08-02 15:45:00', NULL, 0),

-- Draft Mail (DRAFT)
('a0000000-0000-0000-0000-000000000004', 'test@example.com', 'Fragen zur API Dokumentation', 'Hallo Anna,\n\nich wollte bezüglich der Swagger/OpenAPI Doku noch kurz nachfragen, ob...', 'DRAFT', TRUE, FALSE, NULL, NULL, 0),

-- Archived Mail (ARCHIVED)
('a0000000-0000-0000-0000-000000000005', 'erika.musterfrau@example.com', 'Wichtige Information zur Modulprüfung', 'Sehr geehrte Studierende,\n\nbitte beachten Sie die aktuellen Hinweise zur Modulprüfung im Fach Webtechnologien.\n\nMit freundlichen Grüßen,\nErika Musterfrau', 'ARCHIVED', TRUE, TRUE, '2026-07-20 10:00:00', '2026-07-20 10:02:00', 0),

-- Deleted Mail / Papierkorb (DELETED)
('a0000000-0000-0000-0000-000000000006', 'newsletter@external-service.org', 'Wöchentlicher Dev-Newsletter', 'Hallo! Hier sind die wichtigsten News zu Kotlin 2.0, Spring Boot 3 und modernem Webdesign.', 'DELETED', TRUE, FALSE, '2026-07-15 08:00:00', '2026-07-15 08:05:00', 1);

-- ----------------------------------------------------------------------------
-- 3. MAIL RECIPIENTS (mail_to_addresses, mail_cc_addresses)
-- ----------------------------------------------------------------------------
INSERT INTO mail_to_addresses (mail_id, to_email) VALUES
('a0000000-0000-0000-0000-000000000001', 'test@example.com'),
('a0000000-0000-0000-0000-000000000002', 'test@example.com'),
('a0000000-0000-0000-0000-000000000003', 'erika.musterfrau@example.com'),
('a0000000-0000-0000-0000-000000000004', 'anna.schmidt@example.com'),
('a0000000-0000-0000-0000-000000000005', 'test@example.com'),
('a0000000-0000-0000-0000-000000000006', 'test@example.com');

INSERT INTO mail_cc_addresses (mail_id, cc_email) VALUES
('a0000000-0000-0000-0000-000000000003', 'max.mustermann@example.com');

-- ----------------------------------------------------------------------------
-- 4. MAIL USER RELATIONS (mail_user)
-- Mapping emails to users as AUTHOR or RECIPIENT
-- ----------------------------------------------------------------------------
INSERT INTO mail_user (id, user_id, mail_id, relation_type) VALUES
-- Mail 1 (Max -> Test)
('e0000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'RECIPIENT'),
('e0000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'a0000000-0000-0000-0000-000000000001', 'AUTHOR'),

-- Mail 2 (Anna -> Test)
('e0000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000002', 'RECIPIENT'),
('e0000000-0000-0000-0000-000000000004', '22222222-2222-2222-2222-222222222222', 'a0000000-0000-0000-0000-000000000002', 'AUTHOR'),

-- Mail 3 (Test -> Erika)
('e0000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000003', 'AUTHOR'),
('e0000000-0000-0000-0000-000000000006', '33333333-3333-3333-3333-333333333333', 'a0000000-0000-0000-0000-000000000003', 'RECIPIENT'),

-- Mail 4 (Test Draft)
('e0000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000004', 'AUTHOR'),

-- Mail 5 (Erika -> Test, Archived)
('e0000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000005', 'RECIPIENT'),
('e0000000-0000-0000-0000-000000000009', '33333333-3333-3333-3333-333333333333', 'a0000000-0000-0000-0000-000000000005', 'AUTHOR'),

-- Mail 6 (External Newsletter -> Test, Deleted)
('e0000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000006', 'RECIPIENT');
