ALTER TABLE user_app_data
ADD COLUMN  IF NOT EXISTS other_fields jsonb DEFAULT '{}'::jsonb;
