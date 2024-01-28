# Political Speech Evaluation App

This Kotlin application is designed for evaluating political speeches. 
It provides functionality to fetch and analyze CSV data containing political speeches and produces responses based on the analysis.

## Table of Contents
- [Usage](#usage)
- [Controller](#controller)
- [Service](#service)
- [Model](#model)
- [Libraries Used](#libraries-used)


## Usage

To use this application, send a GET request to the `/evaluation` endpoint with a query parameter `urls` containing a map of URLs to evaluate.

Example:

```http
GET /evaluation?url1=political-speeches.csv
```

```http
GET /evaluation?url1=political-speeches.csv&url2=https://example.com/speeches1.csv
```

## Controller

### EvaluationController

The `EvaluationController` handles HTTP requests for speech evaluations.

#### Endpoints

- **GET /evaluation**
    - Evaluates political speeches based on provided URLs.
    - Query Parameter: `urls` - a map of URL pairs to fetch and evaluate speeches.
    - Returns a list of responses or appropriate error messages.

## Service

### EvaluationService

The `EvaluationService` evaluates political speeches.

#### Methods

- **evaluateSpeech**
  - Evaluates a political speech based on a provided URL.
  - Returns a `Response` object.

### CsvParseService

The `CsvParseService` fetches and parses CSV data containing political speeches.

#### Methods

- **fetchAndParseCsv**
    - Fetches and parses CSV content from a provided URL.
    - Returns a pair of URL identifier and a list of `PoliticalSpeech` objects.

- **fetchCsvContent**
    - Fetches CSV content either from a remote URL or a local file path.
    - Returns the CSV content as a string.

## Model

### PoliticalSpeech

The `PoliticalSpeech` data class represents an individual political speech.

### Response

The `Response` data class represents the response generated after evaluating political speeches.


## Libraries Used

### Spring Boot
Used for building and deploying the web application.

### Apache Commons CSV
Used for parsing CSV content.