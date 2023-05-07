CREATE TABLE spaces
(id uuid PRIMARY KEY,
 name VARCHAR(60) NOT NULL,
 section uuid NOT NULL)

--;;

CREATE UNIQUE INDEX "spaces_name_idx" ON spaces(name);

--;;

ALTER TABLE ONLY spaces
  ADD CONSTRAINT spaces_section_fkey FOREIGN KEY ("section") REFERENCES sections(id) ON DELETE CASCADE;
