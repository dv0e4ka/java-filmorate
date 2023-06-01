MERGE INTO RATING_MPA (id, name)
    VALUES (1, 'G');
MERGE INTO RATING_MPA (id, name)
    VALUES (2, 'PG');
MERGE INTO RATING_MPA (id, name)
    VALUES (3, 'PG-13');
MERGE INTO RATING_MPA (id, name)
    VALUES (4, 'R');
MERGE INTO RATING_MPA (id, name)
    VALUES (5, 'NC-17');

MERGE INTO GENRE_TYPES(id, name)
    VALUES (1, 'Комедия');
MERGE INTO GENRE_TYPES(id, name)
    VALUES (2, 'Драма');
MERGE INTO GENRE_TYPES(id, name)
    VALUES (3, 'Мультфильм');
MERGE INTO GENRE_TYPES(id, name)
    VALUES (4, 'Триллер');
MERGE INTO GENRE_TYPES(id, name)
    VALUES (5, 'Документальный');
MERGE INTO GENRE_TYPES(id, name)
    VALUES (6, 'Боевик');




ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FRIENDS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE GENRE_FILM ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE LIKES ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USER_MAN ALTER COLUMN ID RESTART WITH 1;