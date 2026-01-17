# Task: Implementing Exception Handling Layer

Your objective is to ensure application stability by creating a hierarchy of custom exceptions and a global handling mechanism. This will allow the API to return structured error messages instead of raw stack traces.

## Requirements

### 1. Create Custom Exceptions
Implement the exception classes as shown in the project structure (within the `exceptions` package):

- `BadRequestException`
- `ConflictException`
- `ResourceNotFoundException`
- `UserAlreadyExistException`

**Note:** These should extend `RuntimeException`.

### 2. Define the Error Response Structure
Create an `ErrorResponse` class (or record) in the `handler` package. It should contain fields for:

- The error message
- The HTTP status code
- A timestamp

### 3. Implement the GlobalExceptionHandler
Create a `GlobalExceptionHandler` class annotated with `@RestControllerAdvice`.

- Define methods using the `@ExceptionHandler` annotation for each custom exception.
- Each method must catch the specific exception and return a `ResponseEntity<ErrorResponse>` with the appropriate HTTP status (e.g., 404 Not Found for `ResourceNotFoundException`).

## Expected Outcome

Upon successful implementation:

- All new tests must pass (green status).
- This confirms that services are correctly throwing exceptions and the handler is transforming them into the expected API responses.
