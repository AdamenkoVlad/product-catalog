CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          price DECIMAL(19, 2) NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          stock INT NOT NULL,
                          created_date TIMESTAMP NOT NULL,
                          last_updated_date TIMESTAMP NOT NULL
);


INSERT INTO products (name, description, price, category, stock, created_date, last_updated_date)
VALUES
    ('Laptop', 'High performance laptop with 16GB RAM', 999.99, 'Electronics', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Smartphone', 'Latest smartphone with 128GB storage', 699.99, 'Electronics', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Headphones', 'Wireless noise-cancelling headphones', 199.99, 'Electronics', 75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Desk Chair', 'Ergonomic office chair', 249.99, 'Furniture', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Coffee Table', 'Modern wooden coffee table', 149.99, 'Furniture', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Notebook', 'Hardcover notebook with 200 pages', 9.99, 'Stationery', 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);