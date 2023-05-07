CREATE TABLE users_spaces
("user" uuid NOT NULL,
 space uuid NOT NULL,
 PRIMARY KEY ("user", space)
)

--;;

ALTER TABLE ONLY users_spaces
  ADD CONSTRAINT users_spaces_user_fkey FOREIGN KEY ("user") REFERENCES users(id) ON DELETE CASCADE;

--;;

ALTER TABLE ONLY users_spaces
  ADD CONSTRAINT users_spaces_space_fkey FOREIGN KEY (space) REFERENCES spaces(id) ON DELETE CASCADE;

--;;
