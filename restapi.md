# Application RestAPI Controllers

## Admin
### Get all users
curl --location 'http://localhost:8080/topjava/rest/admin/users'

#### Produces: 
[{"id":100001,"name":"Admin","email":"admin@gmail.com","password":"admin","enabled":true,"registered":
"2023-03-28T14:20:47.032+00:00","roles":["USER","ADMIN"],"caloriesPerDay":2000,"meals":null},
{"id":100002,"name":"Guest","email":"guest@gmail.com",
"password":"guest","enabled":true,"registered":"2023-03-28T14:20:47.032+00:00","roles":[],"caloriesPerDay":2000,
"meals":null},{"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,
"registered":"2023-03-28T14:20:47.032+00:00","roles":["USER"],"caloriesPerDay":2000,"meals":null}]
__________
### Get user
curl --location --request POST 'http://localhost:8080/topjava/rest/admin/users/100001'
#### Produces: 
{"id":100001,"name":"Admin","email":"admin@gmail.com","password":"admin","enabled":true,
"registered":"2023-03-28T13:28:32.708+00:00","roles":["ADMIN","USER"],"caloriesPerDay":2000,"meals":null}

__________
### Get user with his meals
curl --location 'http://localhost:8080/topjava/rest/admin/users/100001/with-meals'
#### Produces:
{"id":100001,"name":"Admin","email":"admin@gmail.com","password":"admin","enabled":true,"registered":
"2023-03-28T15:24:54.713+00:00","roles":["USER","ADMIN"],"caloriesPerDay":2000,
"meals":[{"id":100011,"dateTime":"2020-01-31T21:00:00","description":"Админ ужин","calories":1500},
{"id":100010,"dateTime":"2020-01-31T14:00:00","description":"Админ ланч","calories":510}]}
__________
### Create user
curl --location 'http://localhost:8080/topjava/rest/admin/users' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": null,
"name": "newUser",
"email": "newuser@gmail.com",
"password": "newuser",
"enabled": true,
"registered": "2023-03-28T13:28:32.708+00:00",
"roles": [
"ADMIN",
"USER"
],
"caloriesPerDay": 2000
}'
#### Produces:
{"id":100012,"name":"newUser","email":"newuser@gmail.com","password":"newuser","enabled":true,
"registered":"2023-03-28T13:28:32.708+00:00","roles":["ADMIN","USER"],"caloriesPerDay":2000}
__________
### Update user
curl --location --request PUT 'http://localhost:8080/topjava/rest/admin/users/100012' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 100012,
"name": "newUserUpdated",
"email": "newuserUpdated@gmail.com",
"password": "updated",
"enabled": false,
"roles": [
"ADMIN"
],
"caloriesPerDay": 333
}'
#### Produces: 1 (No-content)
__________
### Get user by email
curl --location 'http://localhost:8080/topjava/rest/admin/users/by-email?email=user%40yandex.ru'
#### Produces:
{"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,
"registered":"2023-03-28T13:28:32.708+00:00","roles":["USER"],"caloriesPerDay":2000,"meals":null}
__________
### Delete user
curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/users/100012'
#### Produces: 1 (No-content)

## Profile
### Get profile
curl --location 'http://localhost:8080/topjava/rest/profile'
#### Produces:
{"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,
"registered":"2023-03-28T14:20:47.032+00:00","roles":["USER"],"caloriesPerDay":2000,"meals":null}
__________
### Get profile with meals
curl --location 'http://localhost:8080/topjava/rest/profile/with-meals'
#### Produces:
{"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,"registered":
"2023-03-28T15:29:07.532+00:00","roles":["USER"],"caloriesPerDay":2000,
"meals":[{"id":100009,"dateTime":"2020-01-31T20:00:00","description":"Ужин","calories":510},
{"id":100008,"dateTime":"2020-01-31T13:00:00","description":"Обед","calories":1000},
{"id":100007,"dateTime":"2020-01-31T10:00:00","description":"Завтрак","calories":500},
{"id":100006,"dateTime":"2020-01-31T00:00:00","description":"Еда на граничное значение","calories":100},
{"id":100005,"dateTime":"2020-01-30T20:00:00","description":"Ужин","calories":500},
{"id":100004,"dateTime":"2020-01-30T13:00:00","description":"Обед","calories":1000},
{"id":100003,"dateTime":"2020-01-30T10:00:00","description":"Завтрак","calories":500}]}
__________
### Update profile
curl --location --request PUT 'http://localhost:8080/topjava/rest/profile' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 100000,
"name": "UserUpdated",
"email": "admin@yandex.ru",
"password": "password",
"enabled": true,
"registered": "2023-03-28T09:39:44.986+00:00",
"roles": [
"ADMIN"
],
"caloriesPerDay": 2000
}'
#### Produces: 1 (No-content)
__________
### Delete profile
curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile'
#### Produces: 1 (No-content)
__________

## Meal
### Get all meal
curl --location 'http://localhost:8080/topjava/rest/meals/'
#### Produces: 
[{"id":100015,"dateTime":"2023-02-15T20:00:00","description":"Ужин","calories":777,"excess":false},
{"id":100013,"dateTime":"2023-01-31T20:00:00","description":"Ужин","calories":666,"excess":false},
{"id":100009,"dateTime":"2020-01-31T20:00:00","description":"Обед","calories":222,"excess":false},
{"id":100008,"dateTime":"2020-01-31T13:00:00","description":"Обед","calories":1000,"excess":false},
{"id":100007,"dateTime":"2020-01-31T10:00:00","description":"Завтрак","calories":500,"excess":false},
{"id":100006,"dateTime":"2020-01-31T00:00:00","description":"Еда на граничное значение","calories":100,"excess":false},
{"id":100005,"dateTime":"2020-01-30T20:00:00","description":"Ужин","calories":500,"excess":false},
{"id":100004,"dateTime":"2020-01-30T13:00:00","description":"Обед","calories":1000,"excess":false},
{"id":100003,"dateTime":"2020-01-30T10:00:00","description":"Завтрак","calories":500,"excess":false}]
__________

### Get single meal
curl --location 'http://localhost:8080/topjava/rest/meals/100009'
#### Produces:
{"id":100009,"dateTime":"2020-01-31T20:00:00","description":"Ужин","calories":510,"user":null}
__________

### Create meal
curl --location 'http://localhost:8080/topjava/rest/meals/' \
--header 'Content-Type: application/json' \
--data '{
"id": null,
"dateTime": "2023-02-15T20:00:00",
"description": "Ужин",
"calories": 777
}'
#### Produces:
{"id":100015,"dateTime":"2023-02-15T20:00:00","description":"Ужин","calories":777,"user":null}
__________

### Update meal
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100009' \
--header 'Content-Type: application/json' \
--data '{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Обед",
"calories": 222
}'
#### Produces: 1 (No content)
__________

### Delete meal
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100015'
#### Produces: 1 (No content)
__________

### Get filtered meal by date and time
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=09%3A00' \
--header 'Content-Type: application/json' \
--data '{
"id": null,
"dateTime": "2023-01-31T20:00:00",
"description": "Ужин",
"calories": 510
}'
#### Produces:
[{"id":100009,"dateTime":"2020-01-31T20:00:00","description":"Ужин","calories":510,"excess":true},
{"id":100008,"dateTime":"2020-01-31T13:00:00","description":"Обед","calories":1000,"excess":true},
{"id":100007,"dateTime":"2020-01-31T10:00:00","description":"Завтрак","calories":500,"excess":true}]
__________