create TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL unique (email)
);

create TABLE IF NOT EXISTS tasks (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  task_id VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  task_status VARCHAR(15) NOT NULL,
  task_priority VARCHAR(15) NOT NULL,
  author_id BIGINT NOT NULL,
  performer_id BIGINT,
  CONSTRAINT pk_task PRIMARY KEY (id),
  CONSTRAINT fk_author_id FOREIGN KEY(author_id)
  REFERENCES users(id),
  CONSTRAINT fk_performer_id FOREIGN KEY(performer_id)
  REFERENCES users(id)
);

create TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_id VARCHAR(255) NOT NULL,
    text VARCHAR(255) NOT NULL,
    task_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_task_id FOREIGN KEY(task_id)
    REFERENCES tasks(id),
    CONSTRAINT fk_author_id FOREIGN KEY(author_id)
    REFERENCES users(id)
);