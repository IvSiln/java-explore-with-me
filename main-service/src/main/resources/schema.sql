CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT check_user_email_len CHECK (LENGTH(email) >= 6 AND LENGTH(email) <= 254),
  CONSTRAINT check_user_name_len CHECK (LENGTH(name) >= 2 AND LENGTH(name) <= 250)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(250) NOT NULL,
  CONSTRAINT pk_cat PRIMARY KEY (id),
  CONSTRAINT check_cat_name_len CHECK (LENGTH(name) >= 1 AND LENGTH(name) <= 50)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat FLOAT,
  lon FLOAT,
  CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT REFERENCES categories (id) NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP NOT NULL,
  initiator_id BIGINT REFERENCES users (id) NOT NULL,
  location_id BIGINT REFERENCES locations (id) NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER NOT NULL,
  published_on TIMESTAMP,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(255) NOT NULL,
  title VARCHAR(120) NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT check_event_annotation_len CHECK (LENGTH(annotation) >= 20 AND LENGTH(annotation) <= 2000),
  CONSTRAINT check_event_description_len CHECK (LENGTH(description) >= 20 AND LENGTH(description) <= 7000),
  CONSTRAINT check_event_title_len CHECK (LENGTH(title) >= 3 AND LENGTH(title) <= 120)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_id BIGINT REFERENCES events(id) NOT NULL,
  requester_id BIGINT REFERENCES users(id) NOT NULL,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(120) NOT NULL,
  CONSTRAINT pl_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_events (
  compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
  event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
  PRIMARY KEY(compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  subscriber BIGINT REFERENCES users (id) ON DELETE CASCADE,
  owner BIGINT REFERENCES users (id) ON DELETE CASCADE,
  type VARCHAR(255) NOT NULL,
  CONSTRAINT pk_subscriptions PRIMARY KEY (id)
);
