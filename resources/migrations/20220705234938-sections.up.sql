CREATE TABLE sections
(id uuid PRIMARY KEY,
 name VARCHAR(100) NOT NULL)

--;;

CREATE UNIQUE INDEX "sections_name_idx" ON sections(name);
