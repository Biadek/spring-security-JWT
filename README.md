# spring-security-JWT

Spring application which demonstrates the usage of JSON Web Token for user authentication.

## Technology stack
- Java 11
- Spring Boot
- Spring Security
- Spring Data
- H2
- Hibernate
- Maven

### Registration

```
POST: http://localhost:8080/user/register
```
Header:
```
Content-Type: application/json
```
Body:
```json
{
        "email": "email@gmail.com",
        "password": "password"
}
```
In the received e-mail click activation link.

```
GET: http://localhost:8080/user/token?value=<RECEIVED_TOKEN>
```

### Login

```
POST: http://localhost:8080/user/login
```
Header:
```
Content-Type: application/json
```
Body:
```json
{
        "email": "email@gmail.com",
        "password": "password"
}
```
##Added sample users <br>
email: user@gmail.com <br>
password: password <br>
role: ROLE_USER

email: admin@gmail.com <br>
password: password <br>
role: ROLE_ADMIN

##TODO

In ```application.properties``` set Gmail username and password:

```properties
spring.mail.username=TODO
spring.mail.password=TODO
```
