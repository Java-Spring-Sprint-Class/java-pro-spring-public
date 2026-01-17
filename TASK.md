# Task: Implementing Logging and Writing Custom Tests

Your objective is to improve application observability and reliability by adding a structured logging mechanism and creating custom unit and integration tests. This will help track application behavior and ensure correctness of service logic.

## Requirements

### 1. Implement Logging
- Integrate a logging framework (e.g., `SLF4J` + `Logback`) into the project.
- Add logging statements to all service and controller classes at appropriate levels:
    - `INFO` for key actions (e.g., creating, updating, or deleting resources)
    - `DEBUG` for detailed internal state and flow
    - `ERROR` for exceptions and unexpected behavior

- Ensure that log messages are informative and include relevant identifiers (e.g., userId, issueId) when available.

### 2. Write Custom Unit Tests
- Create unit tests for service classes using `JUnit` and `Mockito`.
- Cover both **happy path** and **edge/bad flows** to ensure correctness.
- Include assertions for:
    - Returned values
    - Expected exceptions
    - Correct logging calls (optional, using tools like `LogCaptor` if desired)

### 3. Write Custom Integration Tests
- Create integration tests for controllers using `@SpringBootTest` and `TestRestTemplate` or `MockMvc`.
- Test that endpoints:
    - Return correct HTTP status codes
    - Handle input validation properly
    - Log relevant actions for traceability

## Expected Outcome

- All custom tests must pass (green status).
- Logs should clearly reflect application flow and errors.
- This confirms that the logging is correctly implemented and the system behaves as expected under both normal and edge conditions.
