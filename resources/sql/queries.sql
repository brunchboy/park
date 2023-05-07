-- :name list-session-ids :? :*
-- :doc retrieves all session ids
SELECT session_id from session_store

-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users (id, name, email, phone, admin, pass)
VALUES (gen_random_uuid(), :name, :email, :phone, :admin, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
   SET name = :name, email = :email, phone = :phone, pass = :pass, admin = :admin
 WHERE id = :id

-- :name list-users :? :*
-- :doc retrieves all user records
SELECT * FROM users
 ORDER by email;

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
 WHERE id = :id

-- :name get-user-by-email :? :1
-- :doc retrieves a user record given the email
SELECT * FROM users
 WHERE email = :email

-- :name update-user-login-timestamp! :! :n
UPDATE users
   SET last_login = now()
 WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
 WHERE id = :id

-- :name create-user-space! :! :n
-- :doc creates a new entry in the users_spaces join table
INSERT INTO users_spaces ("user", space)
VALUES (:user, :space);

-- :name delete-user-space! :! :n
-- :doc removes an entry from the users_spaces join table
DELETE FROM users_spaces
 WHERE "user" = :user
   AND space = :space;


-- :name create-space! :! :n
-- :doc creates a new parking space record
INSERT INTO rooms (id, name, section)
VALUES (gen_random_uuid(), :name, :section)

-- :name list-spaces :? :*
-- :doc retrieves all parking space records
SELECT * FROM spaces

-- :name list-spaces-for-user :? :*
-- :doc retrieves all parking space records assigned to the specified user
SELECT s.*
  FROM spaces s
  INNER JOIN users_spaces us on us.space = s.id
 WHERE ur.user = :user
 ORDER BY r.name

-- :name get-space :? :1
-- :doc retrieves a parking space record given the id
SELECT * FROM spaces
 WHERE id = :id

-- :name delete-room! :! :n
-- :doc deletes a parking space record given the id
DELETE FROM spaces
 WHERE id = :id
