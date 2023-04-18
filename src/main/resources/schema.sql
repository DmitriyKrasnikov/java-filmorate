CREATE TABLE IF NOT EXISTS genre(
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name VARCHAR
);

CREATE TABLE IF NOT EXISTS rating(
        id INTEGER  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name VARCHAR NOT NULL,
        description VARCHAR
);

CREATE TABLE IF NOT EXISTS film(
        id INTEGER PRIMARY KEY,
        name VARCHAR NOT NULL CHECK (name <> ''),
        description VARCHAR(200) NOT NULL,
        realise_date DATE,
        duration INTEGER CHECK (duration >= 1),
        rating_id INTEGER,
        FOREIGN  KEY (rating_id) REFERENCES rating (id) ON DELETE SET NULL,
        CONSTRAINT exist_film_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS film_genres(
        film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
        genre_id INTEGER REFERENCES genre (id) ON DELETE CASCADE,
        PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS user_data (
        id INTEGER PRIMARY KEY,
        login VARCHAR(40) NOT NULL,
        name varchar(40) NOT NULL,
        birthday DATE NOT NULL,
        email VARCHAR   NOT NULL,
        CONSTRAINT user_const CHECK (login <> '' AND email <> '' ),
        CONSTRAINT exist_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS friends(
        user_id INTEGER REFERENCES user_data (id) ON DELETE CASCADE,
        friend_user_id INTEGER REFERENCES user_data (id) ON DELETE CASCADE,
        status BOOLEAN NOT NULL,
        PRIMARY KEY (user_id, friend_user_id)
);

CREATE TABLE IF NOT EXISTS likes(
        user_like_id INTEGER REFERENCES user_data (id) ON DELETE CASCADE,
        film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
        PRIMARY KEY(user_like_id, film_id)
);

