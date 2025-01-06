<div align="left">
  <h1><strong>BackEndFilmesRestAssured</strong></h1>
</div>

## Index

- [About](#about)
- [How to execute the project](#como-executar-o-projeto)
- [Used Technologies](#tecnologias-utilizadas)
- [How to contribute](#como-contribuir)

## About

This project is a demo application built with **Spring Boot** for backend development and **Rest-Assured** for API testing. It is designed to help you understand how to set up and test a Spring Boot application using Rest-Assured and JUnit 5.

This project tests the project: [BackEndFilmes](https://gitlab.com/brunobatista25/BackEndFilmes)

I recommend that you read the BackEndFilmes project ````README.md```` very carefully.

For it to run, you need to have Java 11 initially and then Docker installed on your operating system to test the WiremockController class.

## How to execute the project

1. Clone the project from GitHub: [BackEndFilmesRestAssured](https://github.com/felipeam10/send-book-email-spring-batch)

2. In your preference IDE, open the project from the directory it was cloned to. 

3. Remembering that you read the BackEndFilmes project README.md very carefully.

4. Build the project: `mvn clean install`

5. Running tests: `mvn test -Denv=homolog` or `mvn test -Denv=dev`

### How to generate surefire-report

1. After running your tests with `mvn test -Denv=homolog` or `mvn test -Denv=dev`
2. Run the command `mvn test surefire-report:report -Denv=homolog` or `mvn test surefire-report:report -Denv=dev`
3. This will generate an HTML report in the following directory: `target/site/surefire-report.html`

[Return to Index](#index)


## Technologies Used

Before starting, make sure you have the Java environment correctly configured on your machine. Below you have a list of the technologies that were used in the project development process.

| Technology                 | Version                                    |
|----------------------------|--------------------------------------------|
| Operating System           | Windows 10                                 |
| Programming Language (JDK) | java 17.0.7 2023-04-18 LTS                 
| Framework                  | Spring Boot 3.4.0                          |
| IDE                        | IntelliJ IDEA 2023.1.1 (Community Edition) |

[Return to Index](#index)

## Contributions

If you have a contribution that could improve this project, please open an issue or pull request by following the steps below.

1. Fork the project
2. Create a branch for your modification (`git checkout -b feature/yourFeatureName`)
3. Commit your changes (`git commit -m 'Add some yourFeatureName'`)
4. Push to the branch (`git push origin feature/yourFeatureName`)
5. Open a pull request

[Return to Index](#index)