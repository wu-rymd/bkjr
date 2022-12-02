# Team Kaiserschmarrn

Team repo for Advanced Software Engineering (COMS W4156) / Fall 2022

## Client
- The repo for our client lives [here](https://github.com/wu-rymd/kaiserschmarrn-client).

## About

This service provides an API to manage virtual stock accounts for its (future) clients that include trading apps, financial education software, and data visualization tools.
After registering their users' accounts and by keeping buy/sell transactions up-to-date, the clients are able to gain real-time insights on:

- Account balance (buy power)
- Portfolio value (current worth of all assets owned)
- Profit & Loss (calculated from balance and portfolio value)
- and more!

## How to Run

- Run `./mvnw spring-boot:run`
- Go to [`http://localhost:8080/accounts](http://localhost:8080/accounts)

## API Documentation with Swagger

- Visit [`https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn/v0`](https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn/v0)

## Style checker

- We use the _checkstyle_ plugin from Maven to enforce a consistent style
- To generate a report of style errors, run `./mvnw checkstyle:checkstyle`
  - The report is generated at `/target/checkstyle-result.xml`
  - We copied it to [`/checkstyle/checkstyle-result.xml`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/checkstyle/checkstyle-result.xml) to comply with T3 requirements, since `target/` is git-ignored

## Static analysis

### Branch coverage

- We use the JaCoCo plugin in Maven to track branch coverage of our code base
- To generate a report, run `./mvnw clean verify`
  - The report is generated at `/target/site/jacoco/`
  - We copied it to [`/jacoco`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/jacoco) to comply with T5 requirements, since `target/` is git-ignored
- **Note: `clean verify` clears the `target/` directory. This command regenerates checkstyle and generates JaCoCo reports.**
  - **If you previously generated a FindBugs report (how-to below), you will have to regenerate it. Better yet, always generate the FindBugs report _last_.**
  
### Bug finding

- We use the FindBugs plugin in Maven to find bugs in our code base
- First, run `./mvnw compile` to generate the most up-to-date `.class` files in `target/`
- To generate a report, run `./mvnw findbugs:findbugs`
  - The report is generated at `/target/findbugsXml.xml`
  - We copied it to [`/reports/findbugs/findbugsXml.xml`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/findbugs/findbugsXml.xml) to comply with T5 requirements, since `target/` is git-ignored
- **_Developers:_** Use `./mvnw findbugs:gui` to open a GUI showing the report

## Tests

### Unit Tests
- Run `./mvnw test`
  - Results: Tests run: 76, Failures: 0, Errors: 0, Skipped: 0

### Integration Tests
- Run `./mvnw clean verify -P integration-test`
  - Results: Tests run: 10, Failures: 0, Errors: 0, Skipped: 0

## Deployment

- We deployed the service to an AWS EC2 Instance
  - Visit [`http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/accounts`](http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/accounts)
  - The same API endpoints are available at this address
- We deployed the PostgreSQL database to an AWS RDS Instance
  - The link and credentials are specified in [`src/main/resources/application.properties`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/src/main/resources/application.properties)
