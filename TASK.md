## Task: Validate Entities and DTOs + Controller Validation Tests

### Goal
Ensure that all **Entities** and **DTOs** are properly validated using Bean Validation annotations, and that validation is correctly enforced at the **controller level** through automated tests.

---

### Part 1: Validation of Entities and DTOs

#### 1. Entities Validation
- Add appropriate **Bean Validation annotations** to all entity classes.
- Validation must reflect business rules and database constraints.
- Typical validations include:
    - `@NotNull` – for mandatory fields
    - `@NotBlank` / `@NotEmpty` – for text fields
    - `@Size` – for length constraints
    - `@Min` / `@Max` – for numeric ranges
    - `@Positive` / `@PositiveOrZero`
    - `@Email` – for email fields
- Ensure consistency between:
    - Entity constraints
    - Database schema
    - Business logic expectations

---

#### 2. DTOs Validation
- Add validation annotations to **all request DTOs** used in controllers.
- DTOs must validate **incoming API data** before it reaches the service layer.
- Use:
    - `@NotNull`, `@NotBlank`
    - `@Size`
    - `@Pattern` (if format validation is required)
    - `@Email`, `@Positive`, etc.
- DTO validation must be independent from entity validation.

---

### Part 2: Controller-Level Validation

#### 1. Enable Validation in Controllers
- All controllers that accept request bodies must:
    - Use `@Valid` (or `@Validated`) on DTO parameters.
    - Handle invalid input via standard Spring validation mechanism.
- Validation must fail **before** entering business logic.

---

### Part 3: Controller Tests for Validation (Bad Flow)

#### 1. Test Scope
- For **each controller** that uses `@Valid`:
    - Write **at least one test** that verifies validation failure.
- Focus only on **bad flow scenarios**.

---

#### 2. What Each Test Must Verify
- Send an invalid request DTO (e.g. missing required fields, invalid values).
- Expect:
    - HTTP status `400 BAD REQUEST`
    - Validation error response is returned
- Confirm that:
    - Controller validation is triggered
    - Invalid data does not reach the service layer

---

#### 3. Test Requirements
- Use:
    - `@WebMvcTest` or `@SpringBootTest` (depending on project setup)
    - `MockMvc`
- Mock service layer dependencies.
- Do **not** test business logic — only validation behavior.
- Tests must clearly demonstrate that validation annotations are working.

---

### Acceptance Criteria
- All entities and DTOs contain proper validation annotations.
- All controllers use `@Valid` where required.
- Each validated controller has at least one **bad flow** test.
- Validation errors result in `400 BAD REQUEST`.
- Tests fail if validation annotations are removed or broken.

---

### Notes
- Do not change business logic.
- Validation must be declarative (annotations only).
- Tests should be readable and focused on validation behavior.
