# Back-End Application Programming Project

## Introduction

Welcome to my "Back-End Application Programming Project"! This project demonstrates my expertise in developing object-oriented applications using the Spring framework and integrating them with relational databases.

## Project Overview

In this project, I've migrated a legacy back-end system to the modern Spring framework while integrating it with a MySQL database. The goal is to address undocumented bugs and enhance the system's functionality.

## Key Competencies Demonstrated

- Developing Object-Oriented Applications
- Writing Code with the Spring Framework
- Implementing Design Patterns

## Project Requirements

Here are the essential requirements for this project, summarized in one-liners:

### A. Spring Initializr

- Create a new Java project using Spring Initializr with specific dependencies.

### B. Git Repository

- Create a GitLab repository for the project.
- Connect the Java project to the GitLab repository.
- Commit and push changes for each task completed.

### C. Package Construction

- Construct four new packages: controllers, entities, dao, and services.
- Modify and include RestDataConfig.java in the 'config' package.
- Copy the 'application.properties' file into the application properties resource file.

### D. Entities Package

- Write code for the 'entities' package, including entity classes and an enum designed to match the UML diagram.

### E. Repositories Package

- Write code for the 'dao' package, including repository interfaces for the entities that extend JpaRepository.
- Add cross-origin support.

### F. Services Package

- Write code for the 'services' package, including:
  - A purchase data class with a customer cart and a set of cart items.
  - A purchase response data class containing an order tracking number.
  - A checkout service interface.
  - A checkout service implementation class.

### G. Validation

- Write code to include validation to enforce the inputs needed by the Angular front-end.

### H. Controllers Package

- Write code for the 'controllers' package, including a REST controller checkout controller class with a post mapping to place orders.

### I. Add Customers

- Add five sample customers to the application programmatically without overwriting customer information on subsequent runs.

### J. Run Application

- Run the integrated application by adding a customer order for a vacation with two excursions using the unmodified Angular front-end.
- Provide screenshots showing that the application does not generate a network error and that data was successfully added to the database using MySQL Workbench.

### K. Professional Communication

- Ensure professional communication in the content and presentation of the submission.
