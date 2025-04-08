BEGIN;

-- Criação do usuario admin
INSERT INTO users(email,  password, active, created_at,aplication, access_token) VALUES ( 'admin@ajudaqui.com', '$2a$10$znUFb/sOYO71BXNP/7IeyuNAwlWmt8v9gDtO8qMquA113uzIB1Lh2', true, NOW(), 'authentication_ms','f80d8496-555d-488d-a488-68a190ac876c');
INSERT INTO users_roles(users_id, roles_id) VALUES(1,2);
INSERT INTO users_roles(users_id, roles_id) VALUES(1,3);


END;
