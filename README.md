# java-filmorate
Template repository for Filmorate project.

![Untitled (6)](https://user-images.githubusercontent.com/108143971/234655674-5b833903-4e19-46f1-b2bb-24272359b941.png)



Examples:

1. find likes:

SELECT likes.user.id
FROM film 
LEFT OUTER JOIN likes ON film.id=likes.film_id

2. find user(id=1) friends 

SELECT friend.id_friends
FROM user
LEFT OUTER JOIN friends ON user.id=friends.id_user
WHERE friends.id_user = 1 

3. find film(id=1) genre

SELECT genre.name
FROM film
LEFT OUTER JOIN genre_film ON film.id=genre_film.id_film
LEFT OUTER JOIN genre ON genre_film.id_genre=genre.id

********


p.s.:     `raitnig_mpa` и `friendship_status` у меня `ENUM`

p.p.s.:     В **H2database** есть механизм `ENUM` или просто будет `VARCHAR`, *в зависимости от решения ревьюера (который от ЯП)*

p.p.p.s.:      В **H2database**  `ENUM` - это `Object Types`, не факт, что его можно применять подобным образом
