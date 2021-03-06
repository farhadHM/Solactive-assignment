# My code challenge  

this project is my assignment to begin one of the Solactive Team members :)  

## Getting Started

developing some API to provide real-time price statistics from the last 60 seconds.
### Prerequisites
```
java 14
postman (curl or any other tools for calling api)
```

### Installing

```
please run it with maven Spring Boot plugin 
```

## Built With

* [spring](https://spring.io/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Farhad Yousefi**  - [Farhad](https://github.com/farhadHM/)

## Acknowledgments

* development assumptions 
```
The whole application pipeline (from getting request to sending response) should support concurrency
the statistics related API return double with a whole decimal part 
```
* future improvement
```
better exception handling for web layer (implemention RestControllerAdvice class)
logging enrichment
separation of storing and handling statistics of StatisticsHandler class
better test unit coverage
better documentation and using swagger
using TLS1.2 for secure communication
config spring security and adding oauth2 for calling stateless services 
using Behavior driven design (BDD)   
```
* It was a great pleasure for me to solve this challenge
