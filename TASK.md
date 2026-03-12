## Task Description

The task is to migrate from in-memory data storage to a full database-backed implementation using **Spring Data**.

### What needs to be done:

1. **Add Spring Data dependency**
    - Include `spring-boot-starter-data-jpa`.
    - Configure a database connection (H2 / PostgreSQL / MySQL — depending on project requirements).

2. **Annotate entities**
    - Mark all entity classes with appropriate JPA annotations:
        - `@Entity`
        - `@Table`
        - `@Id`
        - `@GeneratedValue`
        - `@Column`
    - Properly define relationships between entities according to the existing data model.

3. **Repositories**
    - Create repositories extending `JpaRepository` / `CrudRepository` for each entity.
    - Add custom queries if needed.

4. **Refactor code without changing business logic**
    - Replace in-memory collections (`Map`, `List`, `ConcurrentHashMap`, etc.) with repository-based persistence.
    - Preserve the existing business logic and application behavior.
    - All CRUD operations must be performed via the database.

5. **Result**
    - Data is persisted and retrieved from the database.
    - API behavior remains identical to the previous implementation.
    - Controllers and services keep the same contract.

## Development Approach (TDD)

This task must be implemented using the **TDD (Test-Driven Development)** methodology:

- Tests define the expected behavior of the system.
- Implementation must be written to satisfy the existing tests.
- Business logic must **not** be changed — only the persistence mechanism is allowed to be modified.

### ⚠️ Important

- You are **not allowed** to change test cases to match your implementation.
- Your implementation **must match** the behavior described by the tests.

The goal of this task is to learn how to integrate Spring Data JPA into an existing codebase without breaking its architecture or business logic.
