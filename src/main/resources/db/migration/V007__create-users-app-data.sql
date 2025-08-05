CREATE TABLE user_app_data (
  user_id INTEGER NOT NULL,
  app_id INTEGER NOT NULL,
  password VARCHAR(255),
  auth_provider VARCHAR(50) DEFAULT 'email',
  is_active boolean NOT NULL,
  provider_id VARCHAR(255),
  profile_data JSONB DEFAULT '{}'::JSONB,
  last_login TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (user_id, app_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (app_id) REFERENCES applications(id) ON DELETE CASCADE
);

