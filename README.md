# BackEndFilmesRestAssured

This project is a demo application built with **Spring Boot** for backend development and **Rest-Assured** for API testing. It is designed to help you understand how to set up and test a Spring Boot application using Rest-Assured and JUnit 5.

---

## Table of Contents

. [Prerequisites](#prerequisites)
. [Installation](#installation)
. [Running the Application](#running-the-application)
. [Running Tests](#running-tests)
. [Project Structure](#project-structure)
. [Dependencies](#dependencies)
. [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

. **Java Development Kit (JDK)** - Version 17 or later.
- [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
  . **Apache Maven** - Version 3.6 or later.
- [Download Maven](https://maven.apache.org/download.cgi)
  . **Git** (optional) - To clone the repository.
- [Download Git](https://git-scm.com/downloads)
  . A code editor or IDE (e.g., IntelliJ IDEA, Eclipse, or Visual Studio Code).

---

## Installation

Follow these steps to set up the project on your local machine:

. **Clone the Repository** (if applicable):
   ```bash
   git clone https://github.com/your-username/BackEndFilmesRestAssured.git
   cd BackEndFilmesRestAssured

<div align="left">
  <h1><strong>Send Book Email using Spring Batch</strong></h1>
</div>

## Index

- [About](#about)
- [Support Material](#support-material)
- [How to execute the project](#como-executar-o-projeto)
- [Used Technologies](#tecnologias-utilizadas)
- [How to contribute](#como-contribuir)

## About

Summary
In summary, this project is a Spring Batch application that periodically checks for users with book loans nearing their return date and sends them email notifications using SendGrid. The job is scheduled using Quartz, and the batch job consists of reading user data, processing it to create email notifications, and sending those emails.

## Support Material

Access the link: [Support Material](https://drive.google.com/drive/folders/108SAajQwsMlliK6gNmWr3WnAcbf0eu0o)

[Return to Index](#index)

## How to execute the project

1. Clone the project from GitHub

   ````https://github.com/felipeam10/send-book-email-spring-batch````

2. In your preference IDE, open the project from the directory it was cloned to. 

3. Have the docker container installed on your computer. 

Before, locate the file in this project: ````\send-book-email-spring-batch\docker-compose.yml````

Climb structure:

``` 
docker-compose up -d
```

Stop structure:
```
docker-compose down
```
#### Data for connect to phpMyAdmin
- Server: mysql-docker
- User: root / user
- Password: 1234567

#### Data for connect to MySQL Workbench
- Host: 127.0.0.1
- Port: 3307
- User: root / user

4. After to climb your Docker, access phpMyAdmin for manager your database: http://localhost:5050/index.php
5. In "Servidor" type "mysql-docker", use user "root" whith password "1234567".
6. Now, after you logged in database. You will to create the database with name "library". And too you will have create tables and populate her. The script for your database is in the file ````\send-book-email-spring-batch\import.sql````. 
7. In table ````tb_user```` alter the collumn ````email```` to your email to be received.
8. In table ````tb_user_book_loan```` alter the collumn ````loan_date```` to the current date you are in minus 06 days. Change at least one line.

``` 
Important:
Run the query to verify that it returns the results for the changed fields:
```
#### Query to get users who have borrowed books close to return (business rule: books must be returned or renewed within 7 days / Read Step)

```sql
SELECT
    user.id as user_id, 
    user.name as user_name, 
    user.email as user_email, 
    book.id as book_id, 
    book.name as book_name, 
    loan.loan_date 
FROM tb_user_book_loan as loan 
INNER JOIN tb_user as user ON loan.user_id = user.id 
INNER JOIN tb_book as book ON loan.book_id = book.id 
WHERE DATE_ADD(loan_date, INTERVAL 6 DAY) = DATE(NOW());
```

9. Now you will configure the Send Grid Plattform, read the file ````SendGridDetails.txt```` in resource folder. I suffered a lot. I hope you don't suffer as much.
10. In class ````ProcessLoanNotificationEmailProcessorConfig.java```` and in the method ````process```` insert your email that was configurate in Send Grid for to send.
11. In class ````QuartzConfig.java```` and in the method ````jobTrigger```` configure the time for your job execution.


[Return to Index](#index)


## Technologies Used

Before starting, make sure you have the Java environment correctly configured on your machine. Below you have a list of the technologies that were used in the project development process.

| Technology                 | Version                                    |
|----------------------------|--------------------------------------------|
| Operating System           | Windows 10                                 |
| Programming Language (JDK) | java 17.0.7 2023-04-18 LTS                 
| Framework                  | Spring Boot 3.3.3                          |
| Database                   | MySQL                                      |
| IDE                        | IntelliJ IDEA 2023.1.1 (Community Edition) |
| Container                  | Docker v4.24.2                             |

[Return to Index](#index)

## Contributions

If you have a contribution that could improve this project, please open an issue or pull request by following the steps below.

1. Fork the project
2. Create a branch for your modification (`git checkout -b feature/yourFeatureName`)
3. Commit your changes (`git commit -m 'Add some yourFeatureName'`)
4. Push to the branch (`git push origin feature/yourFeatureName`)
5. Open a pull request

[Return to Index](#index)