# Device Price Prediction API

This project provides a RESTful API for managing and predicting device prices based on various specifications. It leverages **Spring Boot** with **H2 Database** for persistence and integrates with a **Flask** application to predict device prices using a machine learning model. The API is structured around CRUD operations for device data, with the ability to send a prediction request to the Flask service to get a price estimate.

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
  - [Endpoints](#endpoints)
  - [Sample Requests](#sample-requests)
- [Database Schema](#database-schema)
- [Error Handling](#error-handling)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

The Device Price Prediction API allows users to manage device data and retrieve price predictions for specific devices. It consists of two main components:

1. **Spring Boot Application**: Manages device data and serves as the API endpoint for CRUD operations.
2. **Flask Application**: Hosts the machine learning model and provides price predictions when requested by the Spring Boot application.

This setup is useful for applications that need a backend API to manage device specifications and fetch price predictions based on a model hosted separately.

## Architecture

The application follows a layered architecture:

- **Controller Layer**: Defines RESTful endpoints for managing devices and requesting predictions.
- **Service Layer**: Contains business logic, including interacting with the Flask API for price prediction.
- **Repository Layer**: Manages database operations for `Device` entities.
- **Model and DTOs**: 
  - `Device` entity maps to the database.
  - `DevicePredictionRequest` DTO maps to the Flask API for predictions.
- **Exception Handling**: Custom exceptions are used to handle specific error cases such as missing devices or prediction failures.

### Flow of Prediction Request

1. The client sends a request to `/api/devices/predict/{id}` (GET request).
2. The controller retrieves the device data from the database and sends a request to the Flask API for a prediction.
3. The Flask API returns the predicted price, which is saved to the device entity and returned to the client.

## Features

- **CRUD Operations**: Create, read, update, and delete device entries.
- **Price Prediction**: Fetches device price prediction from an external Flask API based on device specifications.
- **Error Handling**: Handles device-not-found and prediction-related errors gracefully.
- **H2 Database**: In-memory database for quick testing and easy setup.

## Tech Stack

- **Spring Boot** - Backend framework for building RESTful APIs.
- **H2 Database** - In-memory database for development and testing.
- **Flask** - Hosts the machine learning model for price prediction.
- **ModelMapper** or **MapStruct** - To map data between entities and DTOs.
- **JUnit** - Testing framework.

## Setup and Installation

### Prerequisites

- **Java** 17 or higher
- **Maven** for dependency management
- **Python** with Flask installed (for the prediction service)
- **Git** for version control

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/gawad1/device-price-api.git
   cd device-price-api
   ```

2. **Start the Flask Application**
   - Navigate to the Flask app directory and start the Flask server.
   ```bash
   cd path_to_flask_app
   flask run
   ```
   - The Flask app should be accessible at `http://localhost:5000/predict`.

3. **Run the Spring Boot Application**
   - Ensure you have H2 enabled as the default database in `application.properties`.
   - Build and run the application.
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API Documentation**
   - API documentation is accessible at `http://localhost:8080/api-docs` (if using Swagger) or similar documentation library.

## Usage

### Endpoints

| Method | Endpoint                  | Description                             |
|--------|----------------------------|-----------------------------------------|
| GET    | `/api/devices`             | Get all devices                         |
| GET    | `/api/devices/{id}`        | Get device by ID                        |
| POST   | `/api/devices`             | Add a new device                        |
| GET    | `/api/devices/predict/{id}` | Get predicted price for a specific device |

### Sample Requests

#### 1. Get All Devices
```http
GET /api/devices
```

#### 2. Add a Device
```http
POST /api/devices
Content-Type: application/json

{
  "batteryPower": 5000,
  "blue": true,
  "clockSpeed": 2.3,
  "dualSim": true,
  ...
}
```

#### 3. Predict Device Price
```http
GET /api/devices/predict/1
```

This will retrieve the device with `id=1`, send its data to the Flask API, receive a prediction, and store it in the `predictedPrice` field.

## Database Schema

The `Device` entity contains fields that represent device specifications, such as:

- `id` (Long) - Primary key.
- `batteryPower` (Integer) - Total energy a battery can store.
- `clockSpeed` (Double) - Speed of the microprocessor.
- `blue` (Boolean) - Bluetooth capability.
- `predictedPrice` (Double) - Predicted price of the device.

The database schema is automatically generated by Spring Data JPA. You can view and manage the data via the H2 console at `http://localhost:8081/h2-console` with the appropriate JDBC URL (configured in `application.properties`).

## Error Handling

The application includes custom exception handling:

- **DeviceNotFoundException**: Thrown when a requested device ID does not exist.
- **PredictionException**: Thrown if there is an issue connecting to the Flask API or processing a prediction.

These exceptions are mapped to meaningful HTTP status codes and messages to improve client experience.

## Contributing

If you'd like to contribute:

1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Push to your branch.
5. Submit a Pull Request.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
