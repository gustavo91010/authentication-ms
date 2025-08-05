CREATE TABLE user_app_data (
  id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  app_id BIGINT NOT NULL,
  password VARCHAR(255),
  auth_provider VARCHAR(50) DEFAULT 'email',
  provider_id VARCHAR(255),
  profile_data JSONB DEFAULT '{}'::jsonb,
  last_login TIMESTAMP,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  is_active BOOLEAN NOT NULL DEFAULT true,
  access_token UUID DEFAULT gen_random_uuid(),

  CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_app FOREIGN KEY(app_id) REFERENCES applications(id) ON DELETE CASCADE
);

CREATE TABLE user_app_data_roles (
  user_app_data_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_app_data_id, role_id),
  CONSTRAINT fk_user_app_data FOREIGN KEY(user_app_data_id) REFERENCES user_app_data(id) ON DELETE CASCADE,
  CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);
