CREATE TABLE posts (
                       id IDENTITY PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer (
                          id INT PRIMARY KEY,
                          firstName VARCHAR(100),
                          lastName VARCHAR(100),
                          birthdate VARCHAR(20)
);

CREATE TABLE customer_mt (
                             id INT PRIMARY KEY,
                             firstName VARCHAR(100),
                             lastName VARCHAR(100),
                             birthdate VARCHAR(20)
);