# Porsche Copilot Study

Welcome and thank you very for for beeing part of the Porsche Copilot Study! Your main goal is to finish the experimental task as quick as possible. Feel free to use any ressources you see fit to solve this task (googling, stackoverflow, etc.).

## General instructions

We supplied you with an empty Spring Boot project. Please work within the given structure and nomenclature. This repository also comes with a complete integration test to check if your solution fullfills the specification. Please don't make any change towards it. It is automatically run by GitHub every time you commit any changes. The test results can be seen in the Actions section. Once all the tests run green you successfully finished the experimental task.
After all your test run green we would also like to ask you to fill out a short survey on your demographics and your experience with this task. Please do it after you finished the task so it doesn't count towards your processing time.

## Experimental task specification

In times of digitalization having governance based on pen and paper forms isn’t just outdated but tedious and unnecessary as well. Yet many offices still have paper lists next to their coffee machines. To replace this, we want to introduce a prototype for a web application. The web application should have the following functionalities:

1. An up-to-date overview of all consumers and their respective data must be provided.
2. A consumer must be able to query his own data.
3. A new consumer must be created.
4. A coffee drunk must be recorded on the consumer and paid with their virtual credit account.
5. A consumer must be able to top up their virtual credit account.
6. The credit account must be calculated precisely and without rounding errors.
7. The following errors must be dealt with:
* An attempt is made to access an unknown user.
* A user has insufficient credit to pay for a coffee.
* A user is created that already exists.

This web app should be implemented in Spring Boot 3.2.1 and Java 17 and be built with Maven. Consumer data should be persisted in a relational database. The API should have the following endpoints:

GET /api/v1/consumer
* Returns an array with all consumers

GET /api/v1/consumer/:NAME
* Returns a single consumer with the provided name
* If there is no consumer with that name, it returns a 404 Not found

POST /api/v1/consumer/:NAME
* Adds a new consumer with the provided name to the database and returns the created consumer
* If the consumer already exists it returns a 409 Conflict

PUT /api/v1/consumer/:NAME
* Records consumption by incrementing the count and reducing the credit account by 1.35 in the consumer with the provided name
* Returns the new state of the consumer
* If there is no consumer with that name, it returns a 404 Not found
* If there is no sufficient credit it returns a 402 Payment required

PUT /api/v1/consumer/:NAME/:CREDIT
* Increases a consumer’s credit by the provided amount
* Returns the new state of the consumer
* If there is no consumer with that name, it returns a 404 Not found

The consumer class has the following attributes: id, name, count, credit. The id should be automatically and sequentially generated. Count and credit are initialized with 0. A new consumer should therefor be printed in the following way: Consumer{id=1, name='JohnSmith', count=0, credit=0.00}


## Additional Information

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Proxy Issues
If you work from a protected corporate network your proxy might stop you from connecting to the maven repository to download artifacts and dependencies. You can adress this by adding custom repositories to your pom.xml:
```
<repositories>
    <repository>
        <id>SomeName</id>
        <url>https://Some/URL/</url>
    </repository>
</repositories>
```

