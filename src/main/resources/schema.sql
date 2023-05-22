CREATE TABLE IF NOT EXISTS genre_types (
    id integer,
    name varchar(255) NOT NULL,
    CONSTRAINT genre_types_pk PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS rating_mpa (
    id integer PRIMARY KEY,
    name varchar (50),
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS film (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar (200),
    releaseDate date,
    duration integer,
    mpa integer
);

CREATE TABLE IF NOT EXISTS genre_film (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    id_film integer REFERENCES film(id),
    id_genre integer REFERENCES genre_types(id),
    UNIQUE (id_film, id_genre)
);

CREATE TABLE IF NOT EXISTS user_man (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar(50),
    email varchar(50),
    name varchar(50),
    birthday date
);

CREATE TABLE IF NOT EXISTS friends (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    id_user integer NOT NULL,
    id_friend integer NOT NULL,
    friendship_status BOOLEAN
);

CREATE TABLE IF NOT EXISTS LIKES (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    id_film integer NOT NULL,
    id_user integer NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
   id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   content varchar(5000),
   is_positive BOOLEAN,
   user_id integer NOT NULL,
   film_id integer NOT NULL,
   useful integer
);

CREATE TABLE IF NOT EXISTS review_useful (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    review_id integer NOT NULL,
    user_id integer NOT NULL,
    liked boolean,
    disliked boolean
);


ALTER TABLE genre_film DROP CONSTRAINT IF EXISTS id_film_fk;
ALTER TABLE film DROP CONSTRAINT IF EXISTS mpa_fk;
ALTER TABLE friends DROP CONSTRAINT IF EXISTS id_user_fk;
ALTER TABLE friends DROP CONSTRAINT IF EXISTS id_friend_fk;
ALTER TABLE LIKES DROP constraint IF EXISTS id_film_like_fk;
ALTER TABLE LIKES DROP constraint IF EXISTS id_user_like_fk;

ALTER TABLE reviews DROP CONSTRAINT IF EXISTS user_id_fk;
ALTER TABLE reviews DROP CONSTRAINT IF EXISTS film_id_fk;

ALTER TABLE review_useful DROP CONSTRAINT IF EXISTS review_id_fk;
ALTER TABLE review_useful DROP CONSTRAINT IF EXISTS useful_user_id_fk;

DELETE FROM FRIENDS;
DELETE FROM LIKES;
DELETE FROM review_useful;
DELETE FROM REVIEWS;
DELETE FROM GENRE_FILM;
DELETE FROM USER_MAN;
DELETE FROM FILM;

ALTER TABLE film ADD CONSTRAINT mpa_fk FOREIGN KEY(mpa) REFERENCES rating_mpa(id) ON DELETE CASCADE;
ALTER TABLE genre_film ADD CONSTRAINT id_film_fk FOREIGN KEY(id_film) REFERENCES film(ID) ON DELETE CASCADE;
ALTER TABLE friends ADD CONSTRAINT id_user_fk FOREIGN KEY(id_user) REFERENCES USER_MAN(id) ON DELETE CASCADE;
ALTER TABLE friends ADD CONSTRAINT id_friend_fk FOREIGN KEY(id_friend) REFERENCES USER_MAN(id) ON DELETE CASCADE;
ALTER TABLE LIKES ADD constraint id_film_like_fk FOREIGN KEY(id_film) REFERENCES FILM(ID) ON DELETE CASCADE;
ALTER TABLE LIKES ADD constraint id_user_like_fk FOREIGN KEY(id_user) REFERENCES USER_MAN(ID) ON DELETE CASCADE;

ALTER TABLE reviews ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES user_man (id) ON DELETE CASCADE;
ALTER TABLE reviews ADD CONSTRAINT film_id_fk FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE;

ALTER TABLE review_useful ADD CONSTRAINT review_id_fk FOREIGN KEY(review_id) REFERENCES reviews(id) ON DELETE CASCADE;
ALTER TABLE review_useful ADD CONSTRAINT useful_user_id_fk FOREIGN KEY(user_id) REFERENCES reviews(id) ON DELETE CASCADE;