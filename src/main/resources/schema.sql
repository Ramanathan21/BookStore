DROP TABLE IF EXISTS category, book, author, book_author, book_category;

CREATE TABLE category (
  category_id IDENTITY PRIMARY KEY, -- using identity to auto_increment the id
  name VARCHAR(255) NOT NULL
);

CREATE TABLE author (
  author_id IDENTITY PRIMARY KEY,
--  author_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL
);

CREATE TABLE book (
  isbn VARCHAR(20) PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  publisher VARCHAR(255) NOT NULL
);

CREATE TABLE book_author (
  isbn VARCHAR(255) NOT NULL,
  author_id INT NOT NULL,
  PRIMARY KEY (isbn, author_id),
  FOREIGN KEY (isbn) REFERENCES book(isbn),
  FOREIGN KEY (author_id) REFERENCES author(author_id)
);

CREATE TABLE book_category (
  isbn VARCHAR(255) NOT NULL,
  category_id INT NOT NULL,
  PRIMARY KEY (isbn, category_id),
  FOREIGN KEY (isbn) REFERENCES book(isbn),
  FOREIGN KEY (category_id) REFERENCES category(category_id)
);