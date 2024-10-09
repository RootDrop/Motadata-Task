# Java Spring Boot CRUD Application

This is a simple CRUD (Create, Read, Update, Delete) application built using Java Spring Boot framework. It demonstrates basic operations for managing data in a database.

# Features
Create: Add new entries to the database.<br>
Read: Retrieve and display data from the database.<br>
Update: Modify existing entries in the database.<br>
Delete: Remove entries from the database.
 
# Requirements
Java 17<br>
PostgreSQL database

# Setup

Clone the repository:<br>
1. https://github.com/RootDrop/VNH.git <br>
2. cd vhn

Configure the database:<br>
1. Create a database crud
2. Update the application.properties file located in src/main/resources with your database configuration.

Build and run the application:<br>
1. mvn spring-boot:run

Access the application:<br>
1. Once the application is running, you can access it at http://localhost:8080.

# Usage
1. Use the provided API endpoints to perform CRUD operations on the data.
2. Refer to the API documentation or explore the codebase to understand how to interact with the application.

# API Endpoints
1. /api/customer : create new customer
2. /api/customer : retrieve all customers
3. /api/customer/{id} : retrieve specific customer
4. /api/customer : update existing customer
5. /api/customer : delete new customer

# Contributing
Contributions are welcome! If you find any issues or want to add new features, feel free to open a pull request.


