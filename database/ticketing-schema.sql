CREATE TABLE app_user (
  id         SERIAL,
  user_name  VARCHAR(100) NOT NULL,
  user_email VARCHAR(100) NOT NULL,
  rol        VARCHAR(50)  NOT NULL,
  password   VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (user_name)
);

CREATE TABLE client (
  id          SERIAL,
  app_user_id INTEGER      NOT NULL,
  client_name VARCHAR(250) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (app_user_id) REFERENCES app_user (id)
);

CREATE TABLE ticket (
  id                SERIAL,
  client_id         INTEGER       NOT NULL,
  operator_id       INTEGER       NOT NULL,
  coordinator_id    INTEGER,
  channel           VARCHAR(50)   NOT NULL,
  contact_name      VARCHAR(250)  NOT NULL,
  subject           VARCHAR(1024) NOT NULL,
  description       TEXT,
  status            VARCHAR(50)   NOT NULL,
  escalated         BOOLEAN       NOT NULL,
  resolution        VARCHAR(50),
  resolution_info   TEXT,
  creation_date     TIMESTAMP     NOT NULL,
  modification_date TIMESTAMP     NOT NULL,
  closing_date      TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (client_id) REFERENCES client (id),
  FOREIGN KEY (operator_id) REFERENCES app_user (id),
  FOREIGN KEY (coordinator_id) REFERENCES app_user (id)
);