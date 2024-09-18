CREATE TYPE task_status AS ENUM ('draft', 'ready_for_approval', 'approved');
CREATE TYPE submission_mode AS ENUM ('run', 'diagnose', 'submit');

CREATE CAST (CHARACTER VARYING as task_status) WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as submission_mode) WITH INOUT AS IMPLICIT;


CREATE TABLE task
(
    id            BIGINT        NOT NULL,
    max_points    NUMERIC(7, 2) NOT NULL,
    status        TASK_STATUS   NOT NULL,
    solution      TEXT          NOT NULL, -- custom column
    INSERT_STATEMENTS TEXT,
    SCHEMA_NAME VARCHAR(50) NOT NULL UNIQUE,
    TABLE_POINTS integer NOT NULL,
    COLUMN_POINTS integer NOT NULL,
    PRIMARYKEY_POINTS integer NOT NULL,
    FOREIGNKEY_POINTS integer NOT NULL,
    CONSTRAINT_POINTS integer NOT NULL,
    CONSTRAINT task_pk PRIMARY KEY (id)
);

CREATE TABLE submission
(
    id                UUID                     DEFAULT gen_random_uuid(),
    user_id           VARCHAR(255),
    assignment_id     VARCHAR(255),
    task_id           BIGINT,
    submission_time   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    language          VARCHAR(2)      NOT NULL DEFAULT 'en',
    mode              submission_mode NOT NULL,
    feedback_level    INT             NOT NULL,
    evaluation_result JSONB,
    submission        TEXT, -- custom column
    CONSTRAINT submission_pk PRIMARY KEY (id),
    CONSTRAINT submission_task_fk FOREIGN KEY (task_id) REFERENCES task (id)
        ON DELETE CASCADE
);

CREATE TABLE connections
(
    ID integer PRIMARY KEY,
    SCHEMA_NAME VARCHAR(20) NOT NULL UNIQUE,
    CONN_USER VARCHAR(100) NOT NULL,
    CONN_PWD VARCHAR(100) NOT NULL
);

----------------------------------------
-- Insert users in connections table
----------------------------------------
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl1', 'ddl1', 'ddl1');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (2, 'ddl2', 'ddl2', 'ddl2');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (3, 'ddl3', 'ddl3', 'ddl3');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (4, 'ddl4', 'ddl4', 'ddl4');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (5, 'ddl5', 'ddl5', 'ddl5');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (6, 'ddl6', 'ddl6', 'ddl6');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (7, 'ddl7', 'ddl7', 'ddl7');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (8, 'ddl8', 'ddl8', 'ddl8');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (9, 'ddl9', 'ddl9', 'ddl9');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (10, 'ddl10', 'ddl10', 'ddl10');
