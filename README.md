# lego-collector
A simple lego collector project for Advanced Programming Techniques at Unifi.

###### Continuous integration (by [GitHub Actions](https://github.com/features/actions)):
[![CI tests](https://github.com/AngeloDamante/lego-collector/actions/workflows/maven.yml/badge.svg)](https://github.com/AngeloDamante/lego-collector/actions/workflows/maven.yml)

###### Code coverage (by [Coveralls](https://coveralls.io/)):
[![Coverage Status](https://coveralls.io/repos/github/AngeloDamante/lego-collector/badge.svg?branch=main)](https://coveralls.io/github/AngeloDamante/lego-collector?branch=main)

###### Code quality (by [SonarCloud](https://www.sonarsource.com/products/sonarcloud/)):
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
</br>
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
</br>
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
</br>
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=AngeloDamante_lego-collector&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=AngeloDamante_lego-collector)

## Layout Directories
```
.
├── lego-collector
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java/com/angelodamante/app
│       │   │   ├── controller
│       │   │   │   └── LegoController.java
│       │   │   ├── launcher
│       │   │   │   └── LegoApp.java
│       │   │   ├── model
│       │   │   │   └── LegoEntity.java
│       │   │   ├── repository
│       │   │   │   └── LegoRepository.java
│       │   │   └── view
│       │   │       ├── LegoSwingView.java
│       │   │       └── LegoView.java
│       │   └── resources
│       │       └── log4j2.xml
│       └── test
│           ├── java/com/angelodamante/app
│           │   ├── controller
│           │   │   └── LegoControllerTest.java
│           │   └── view
│           │       └── LegoSwingViewTest.java
│           └── resources
│               └── log4j2.xml
└── README.md

```
