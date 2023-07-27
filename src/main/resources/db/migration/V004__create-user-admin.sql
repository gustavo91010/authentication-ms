BEGIN;

-- Criação do usuario admin
INSERT INTO users(username,  password, active, created_at) VALUES ( 'admin@ajudaqui.com', '$2a$10$7g24OsT/sMhEeZLyoSPepexj/NwQ7OiDTXpe6pHPDenIsufrtMBGG', true, NOW());
INSERT INTO users_roles(users_id, roles_id) VALUES(1,2);
INSERT INTO users_roles(users_id, roles_id) VALUES(1,3);


END;