BEGIN;

-- Criação do usuario admin
INSERT INTO users(email,  password, active, created_at,aplication) VALUES ( 'admin@ajudaqui.com', '$2a$10$7g24OsT/sMhEeZLyoSPepexj/NwQ7OiDTXpe6pHPDenIsufrtMBGG', true, NOW(), 'authentication_ms');
INSERT INTO users_roles(users_id, roles_id) VALUES(1,2);
INSERT INTO users_roles(users_id, roles_id) VALUES(1,3);


END;