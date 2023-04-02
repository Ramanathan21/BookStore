-- Categories --
INSERT INTO category (category_id, name) VALUES
 (1, 'Technical'),
 (2, 'Literature'),
 (3, 'Humour'),
 (4, 'Poetry'),
 (5, 'Science Fiction'),
 (6, 'Self Help'),
 (7, 'Psychology');

ALTER TABLE category ALTER COLUMN category_id RESTART WITH 8;

-- Books --
INSERT INTO book (isbn, title, publisher) VALUES
  ('0-201-63361-2', 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Addison-Wesley'),
  ('81-7992-192-X', 'The Monk Who Sold Ferrari', 'Jaico Books'),
  ('978-1-84-794183-1', 'Atomic Habits', 'Penguin');

-- Authors --
INSERT INTO author (author_id, first_name, last_name) VALUES
  (1, 'Erich', 'Gamma'),
  (2, 'Richard', 'Helm'),
  (3, 'Ralph', 'Johnson'),
  (4, 'John', 'Vlissides'),
  (5, 'Robin', 'Sharma'),
  (6, 'James', 'Clear');

ALTER TABLE author ALTER COLUMN author_id RESTART WITH 7;

-- Book Authors mapping --
INSERT INTO book_author (isbn, author_id) VALUES
  ('0-201-63361-2', 1),
  ('0-201-63361-2', 2),
  ('0-201-63361-2', 3),
  ('0-201-63361-2', 4),
  ('81-7992-192-X', 5),
  ('978-1-84-794183-1', 6);

INSERT INTO book_category (isbn, category_id) VALUES
  ('0-201-63361-2', 1),
  ('81-7992-192-X', 6),
  ('978-1-84-794183-1', 6),
  ('978-1-84-794183-1', 7);