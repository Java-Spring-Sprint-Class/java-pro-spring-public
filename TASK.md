# Sprint 08: JWT Authentication Integration

## Sprint Goal
Transition from basic authorization (HTTP Basic) to a modern **stateless architecture** using **JSON Web Tokens (JWT)**.

In this sprint, you will implement a complete mechanism for issuing, validating, and using access tokens. The API will no longer rely on server-side sessions and will be ready for integration with any frontend client (React, Angular, Mobile).

---

## Technical Requirements

### 1. Dependency Preparation
- Add the required JWT libraries to your `pom.xml`.
- It is recommended to use the `io.jsonwebtoken` (`jjwt`) library.

---

### 2. Configuration (`application.properties`)
- Move the secret key and token lifetime into application configuration.
- Do **not** hardcode these values in Java code.

Required properties:
- `tokenKey`: A long random string (minimum 256 bits for HS256 algorithm).
- `accessTokenValidTimeInMillisecond`: Token lifetime (e.g. 120 minutes).

---

### 3. JwtTool Implementation (Utility Class)
Create a component responsible for all JWT-related logic. It must be able to:

- **Generate token (`generateToken`)**
    - Create a JWT containing:
        - username
        - user ID
        - list of user roles (stored in claims)

- **Validate token (`validateToken`)**
    - Verify the token signature
    - Check token expiration

- **Extract data**
    - `extractUsername`
    - `extractAllClaims`
    - Used later for authorization logic

**Important:**  
When generating the token, ensure that user roles are included in the claims. This allows the client to know user permissions without making additional API calls.

---

### 4. Authorization Filter (`AccessTokenAuthenticationFilter`)
Implement a filter that intercepts every HTTP request (extend `OncePerRequestFilter`).

Filter workflow:
1. Check for the presence of the `Authorization` header.
2. If the header starts with `Bearer `, extract the token.
3. Validate the token using `JwtTool`.
4. If the token is valid:
    - Load the user via `UserDetailsService`.
    - Place the authentication object into `SecurityContextHolder`.

---

### 5. Security Configuration Update
Update the security configuration as follows:

- Disable `httpBasic`.
- Set session policy to `SessionCreationPolicy.STATELESS` (no server-side sessions).
- Add `AccessTokenAuthenticationFilter` **before** `UsernamePasswordAuthenticationFilter`.
- Allow public access (`permitAll`) to:
    - `POST /api/users/register`
    - `POST /api/users/login`
- Configure an `authenticationEntryPoint` to return **401 Unauthorized** when accessing secured endpoints without a token.

---

### 6. Login Logic (`UserService` and `UserController`)

#### DTOs
- `LoginRequestDto`: `username`, `password`
- `TokenResponseDto`: `accessToken`, user information

#### Service
Implement a `login` method that:
1. Uses `AuthenticationManager` to validate credentials.
2. Generates a JWT using `JwtTool` if authentication is successful.
3. Returns a response containing the access token.

#### Controller
- Add endpoint: `POST /api/users/login`
- Accepts credentials and returns a token response.

---

## Acceptance Criteria

### Login Flow
- Sending valid credentials to `/api/users/login` returns:
    - JSON with `accessToken`
    - HTTP status `200 OK`
- Sending invalid credentials returns:
    - `401 Unauthorized` or `400 Bad Request`

---

### Registration Flow
- After successful registration, the user immediately receives a token (automatic login).
- The user should not be required to enter credentials again.

---

### Access Flow
- Requesting a protected resource (e.g. `GET /api/users`) **without** an `Authorization` header returns:
    - `401 Unauthorized`
- Requesting the same resource **with** `Authorization: Bearer <TOKEN>` returns:
    - `200 OK`

---

### Role Security
- A user with role `USER` cannot access `ADMIN` endpoints even with a valid token.
- The API must return `403 Forbidden` in this case.

---

### Tests
- Integration tests for controllers must pass.
- Use:
    - `@WithMockUser`, or
    - Disable security filters for specific validation tests where appropriate.

---

## Hints
- For Postman testing, use the **Authorization → Bearer Token** tab.
- `AuthenticationManager` must be explicitly declared as a `@Bean` in the security configuration to be injectable.
- Pay attention to exception handling:
    - If a token is expired, `JwtTool` will throw an exception.
    - The filter should handle this gracefully (e.g. do not set authentication and allow the `EntryPoint` to return `401`).
