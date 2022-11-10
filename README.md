# Team Kaiserschmarrn

Team repo for Advanced Software Engineering (COMS W4156) / Fall 2022

## About

This service provides an API to manage virtual stock accounts for its (future) clients that include trading apps, financial education software, and data visualization tools.
After registering their users' accounts and by keeping buy/sell transactions up-to-date, the clients are able to gain real-time insights on:

- Account balance (buy power)
- Portfolio value (current worth of all assets owned)
- Profit & Loss (calculated from balance and portfolio value)
- and more!

## How to Run

- Run `./mvnw spring-boot:run`
- Go to [`http://localhost:8080/accounts/<ACCOUNT_ID>`](http://localhost:8080/accounts/boraelci)

## API Documentation with Swagger

- Available at external link: [`https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn/v0`](https://app.swaggerhub.com/apis-docs/BORAELCI/kaiserschmarrn/v0)
- Alternatively, you can visit [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html) after running the service
- Similarly, [`http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/swagger-ui/index.html`](http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/swagger-ui/index.html)

## Style checker

- We use the _checkstyle_ plugin from Maven to enforce a consistent style
- To generate a report of style errors, run `./mvnw checkstyle:checkstyle`
  - The report is generated at `/target/checkstyle-result.xml`
  - We copied it to [`/checkstyle/checkstyle-result.xml`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/checkstyle/checkstyle-result.xml) to comply with T3 requirements, since `target/` is git-ignored

## Deployment

- We deployed the service to an AWS EC2 Instance
  - Accessible at [`http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/accounts/<ACCOUNT_ID>`](http://ec2-35-174-136-81.compute-1.amazonaws.com:8080/accounts/boraelci)
  - The same API endpoints are available at this address
- We deployed the PostgreSQL database to an AWS RDS Instance
  - The link and credentials are specified in [`src/main/resources/application.properties`](https://github.com/wu-rymd/kaiserschmarrn/blob/main/src/main/resources/application.properties)
