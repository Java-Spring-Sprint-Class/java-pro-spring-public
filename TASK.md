# Task: Implement Security in Spring Boot Application

## Objective
The goal of this task is to implement **Spring Security** in the application with proper access control, custom `UserDetails` and `UserDetailsService`, and update existing tests to work with security.

---

## Requirements

### 1. Security Configuration
- Configure **Spring Security** to allow **everyone** to access only the `/register` endpoint in the `UserController`.
- Restrict access to the `RoleController` endpoints to **ADMIN** users only.
- All other endpoints should be secured according to the application's requirements.

### 2. Custom UserDetails
- Implement a **custom `UserDetails`** class that wraps the `User` entity.
- Map roles and permissions from your entities to Spring Security authorities.

### 3. UserDetailsService
- Implement a **custom `UserDetailsService`** that loads user information from the database.
- Ensure proper exception handling when users are not found.

### 4. Update Tests
- Refactor existing tests to include **security context**.
- Write tests to verify:
    - Public endpoints (e.g., `/register`) are accessible without authentication.
    - Admin endpoints (e.g., `/roles`) are restricted to users with the `ADMIN` role.
    - Unauthorized access returns appropriate HTTP status codes (e.g., `403 Forbidden`).

### 5. Bonus
- Use **TDD approach**: implement tests first and then secure the application to pass those tests.
- Ensure code is modular and easy to extend for future roles and permissions.

---

## Deliverables
1. `SecurityConfig.java` or equivalent configuration.
2. `CustomUserDetails.java` and `CustomUserDetailsService.java`.
3. Updated and fully working tests with security enabled.
4. Documentation or comments explaining security rules applied to controllers.

---

## Notes
- Make sure to use **Spring Boot 3+** and **Spring Security 6+** conventions.
- Use annotations like `@PreAuthorize` or `@Secured` where needed for method-level security.
- Keep password encoding and authentication best practices in mind.
