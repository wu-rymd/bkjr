# Team Kaiserschmarrn

Team repo for Advanced Software Engineering (COMS W4156) / Fall 2022

## Client

- Repo: [https://github.com/wu-rymd/kaiserschmarrn-client/tree/main](https://github.com/wu-rymd/kaiserschmarrn-client)

## About

This service provides an API to manage virtual stock accounts for its (future) clients that include trading apps, financial education software, and data visualization tools.
After registering their users' accounts and by keeping buy/sell transactions up-to-date, the clients are able to gain real-time insights on:

- Account balance (buy power)
- Portfolio value (current worth of all assets owned)
- Profit & Loss (calculated from balance and portfolio value)
- and more!

## Start

- Run `./mvnw spring-boot:run`
- Open Postman, and follow these steps
- You need to retrieve an access token from our `/auth/login/ endpoint. In order to accomplish this:
- Select 'raw' and 'JSON' from the dropdowns in the Body tab. Copy & paste these lines:
```
{
 "clientId": "ftx_exchange",
 "password": "12345678"
}
```
- Then, send a POST request to `http://localhost:8080/auth/login`
- Copy the access token returned in the response. Select the 'Bearer Token' in the Auth tab and paste it to the Token field
- Send a GET request to `http://localhost:8080/accounts` to retrieve a list of accounts
- Explore more endpoints listed in our API Documentation!
- You can repeat these steps for other clientIds such as "binance", with the same password

## API Documentation with Swagger

- Visit [`https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn`](https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn)

## Static analysis

### Branch coverage

- We use the JaCoCo plugin in Maven to track branch coverage of our code base
- To generate a report, run `./mvnw clean verify`
  - The report is generated at `/target/site/jacoco/`
  - We copied it to [`/reports/jacoco`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/jacoco) to comply with T5 requirements, since `target/` is git-ignored
- **Note: `clean verify` clears the `target/` directory and generates the JaCoCo report.**
  - **If you previously generated a FindBugs report (how-to below), you will have to regenerate it. Better yet, always generate the branch coverage report **first**.**
  - **If you previously generated a checkstyle report (how-to below), you will have to regenerate it. Better yet, always generate the branch coverage report **first**.**

### Bug finding

- We use the FindBugs plugin in Maven to find bugs in our code base
- First, run `./mvnw compile` to generate the most up-to-date `.class` files in `target/`
- To generate a report, run `./mvnw findbugs:findbugs`
  - The report is generated at `/target/findbugsXml.xml`
  - We copied it to [`/reports/findbugs/findbugsXml.xml`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/findbugs/findbugsXml.xml) to comply with T5 requirements, since `target/` is git-ignored
- **_Developers:_** Use `./mvnw findbugs:gui` to open a GUI showing the report

## Style checker

- We use the _checkstyle_ plugin from Maven to enforce a consistent style
- To generate a report of style errors, run `./mvnw checkstyle:checkstyle`
  - The report is generated at `/target/checkstyle-result.xml`
  - We copied it to [`/reports/checkstyle/checkstyle-result.xml`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/checkstyle/checkstyle-result.xml) to comply with T3 requirements, since `target/` is git-ignored

## Tests

### Unit Tests

- Run `./mvnw test`
  - Results: Tests run: 76, Failures: 0, Errors: 0, Skipped: 0

### Integration Tests

- Run `./mvnw clean verify -P integration-test`
  - Results: Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
  
### End-to-End Tests
- Documented at [`/reports/end-to-end-tests.txt`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/reports/end-to-end-tests.txt)

## Deployment

- We deployed the service to an AWS EC2 Instance
  - Use Postman to retrieve your access token from `http://ec2-3-83-151-62.compute-1.amazonaws.com:8080/auth/login`. Note: this link may go outdated
  - Visit other endpoints using Postman in the same way explained under the Start section
- We deployed the PostgreSQL database to an AWS RDS Instance
  - The link and credentials are specified in [`src/main/resources/application.properties`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/src/main/resources/application.properties)
