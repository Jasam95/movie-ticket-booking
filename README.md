A Spring Boot + Thymeleaf + MySQL web application that allows users to browse movies, book tickets, and manage theaters and shows.
Admins can manage movies, screens, and schedules through an admin dashboard.
The system supports authentication, authorization, and Swagger API docs for backend testing.

**Tech Stack:**
Layer	Technology
Backend	Spring Boot 3, Spring MVC, Spring Security
Frontend	Thymeleaf, Bootstrap 5
Database	MySQL / H2 (for tests)
ORM	Hibernate / JPA
Tools	Swagger (springdoc-openapi), Maven, Docker, Railway
Tests	JUnit 5, Mockito

**Features:**
1. User Registration & Login

2. Movie management (CRUD)

3. Theater & Screen management

4. Show scheduling

5. Ticket booking with seat selection

6. Booking history per user

7. Swagger REST API Documentation

**Users:**
| Role  | Username (email)    | Password   |
| ----- | ------------------- | ---------- |
| Admin | `admin@123.com`     | `admin@123`   |
| User  | `user1@123.com`     | `user1@123`   |


**Entity flow diagram:**
User (1) --------< Booking >-------- (1) Show
                                |           |
                                V           V
                         Movie (1)      Screen (1)
                                            |
                                            V
                                       Theater (n)

**Description:**

User can have multiple Bookings

Booking belongs to a specific Show

Show is linked to one Movie and one Screen

Screen belongs to a Theater



**Railway Deployment Notes**
Push latest code to GitHub
In Railway → “New Project” → “Deploy from GitHub”

Add Environment Variables:

spring.datasource.url=jdbc:mysql://root:WcGpxsyIWEkcIbgcojRORdAJqVuyiYYD@shortline.proxy.rlwy.net:39015/railway

spring.datasource.username=root

spring.datasource.password=WcGpxsyIWEkcIbgcojRORdAJqVuyiYYD



**Swagger API**
Main Endpoints

GET	/api/movies	Fetch all movies

POST	/api/movies	Create a new movie

GET	/api/theaters	Get all theaters

POST	/api/bookings/confirm	Confirm booking

GET	/api/shows	Fetch shows by movie
                                
                             
                                     

