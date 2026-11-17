
-- Create Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255),
    is_admin BOOLEAN NOT NULL
);

-- Create Books Table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Create Borrowed Books Table
CREATE TABLE borrowed_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Create Booklists Table
CREATE TABLE booklists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(255) NOT NULL,
    is_public BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Booklist Books Table (Many-to-Many relation between Booklists and Books)
CREATE TABLE booklist_books (
    booklist_id INT,
    book_id INT,
    PRIMARY KEY (booklist_id, book_id),
    FOREIGN KEY (booklist_id) REFERENCES booklists(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Insert Sample Users
INSERT INTO users (username, password, is_admin) VALUES
('admin', 'admin123', TRUE),
('alice', NULL, FALSE),
('bob', NULL, FALSE);

-- Insert Sample Books
INSERT INTO books (title, author, price) VALUES
('Java Programming', 'John Doe', 49.99),
('Database Systems', 'Jane Smith', 39.95),
('Swing in Java', 'Emily White', 29.99),
('Data Structures', 'Robert Brown', 45.00);

-- Insert Sample Booklists
INSERT INTO booklists (user_id, name, is_public) VALUES
(2, 'Alice\'s Favorites', TRUE),
(3, 'Bob\'s Secrets', FALSE);

-- Insert Books into Booklists
INSERT INTO booklist_books (booklist_id, book_id) VALUES
(1, 1), (1, 2),
(2, 3), (2, 4);


