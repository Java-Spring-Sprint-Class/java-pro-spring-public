# AgileNext Junior Java Developer Mission

Welcome to **AgileNext Junior Java Developer Mission**.

You are the new **Junior Java Developer** at the startup AgileNext. Our Tech Lead left a prototype of the core system of our future project management tool (a Jira-like application).

Your mission: transform this prototype into a full-fledged, working **REST API application**. Ensure the system is reliable, the code is clean, and tests are green.

---

## Entities

### 1. User

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| username | String | `username` |
| email | String | `email` |
| passwordHash | String | `passwordHash` |
| avatarUrl | String | `avatarUrl` |
| isActive | Boolean | `isActive` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 2. Role

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| name | String | `name` |

### 3. UserRole (Many-to-Many)

| Field | Type | Variable Name |
|-------|------|---------------|
| userId | Long | `userId` |
| roleId | Long | `roleId` |

### 4. Project

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| name | String | `name` |
| key | String | `key` |
| ownerId | Long | `ownerId` |
| description | String | `description` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 5. ProjectMember

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| projectId | Long | `projectId` |
| userId | Long | `userId` |
| role | ProjectRoleType | `role` |

### 6. Issue

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| projectId | Long | `projectId` |
| key | String | `key` |
| title | String | `title` |
| description | String | `description` |
| type | IssueType | `type` |
| priority | Priority | `priority` |
| statusId | Long | `statusId` |
| assigneeId | Long | `assigneeId` |
| reporterId | Long | `reporterId` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 7. Status

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| name | String | `name` |
| category | StatusCategory | `category` |
| position | Integer | `position` |
| projectId | Long | `projectId` |

### 8. IssueComment

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| issueId | Long | `issueId` |
| userId | Long | `userId` |
| content | String | `content` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 9. Attachment

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| issueId | Long | `issueId` |
| userId | Long | `userId` |
| fileName | String | `fileName` |
| fileUrl | String | `fileUrl` |
| fileSize | Integer | `fileSize` |
| createdAt | LocalDateTime | `createdAt` |

### 10. IssueHistory

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| issueId | Long | `issueId` |
| userId | Long | `userId` |
| fieldChanged | String | `fieldChanged` |
| oldValue | String | `oldValue` |
| newValue | String | `newValue` |
| createdAt | LocalDateTime | `createdAt` |

### 11. Label

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Long | `id` |
| name | String | `name` |
| color | String | `color` |

### 12. IssueLabel (Many-to-Many)

| Field | Type | Variable Name |
|-------|------|---------------|
| issueId | Long | `issueId` |
| labelId | Long | `labelId` |

---

## Enums

```java
public enum IssueType { BUG, TASK, STORY, EPIC }
public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
public enum StatusCategory { TO_DO, IN_PROGRESS, DONE }
public enum ProjectRoleType { OWNER, ADMIN, MEMBER, VIEWER }
```

## Services (Methods)

### UserService

| Method         | Parameters                                     | Description                                            |
| -------------- | ---------------------------------------------- | ------------------------------------------------------ |
| register       | String username, String email, String password | Creates a new user. Password should be hashed.         |
| getUser        | Long id                                     | Retrieves a user by ID.                                |
| listUsers      | String search                                  | Returns a list of users. Filters by username or email. |
| updateProfile  | Long id, String username, String email      | Updates username and email. Updates `updatedAt`.       |
| deactivateUser | Long id                                     | Deactivates user (`isActive=false`).                   |
| assignRole     | Long userId, Long roleId                 | Assigns role to user.                                  |
| removeRole     | Long userId, Long roleId                 | Removes role from user.                                |

### RoleService

| Method     | Parameters  | Description                          |
| ---------- | ----------- | ------------------------------------ |
| createRole | String name | Creates a new role. Returns role ID. |
| getRoles   | —           | Returns all roles.                   |

### ProjectService

| Method        | Parameters                                              | Description                                                                |
| ------------- | ------------------------------------------------------- | -------------------------------------------------------------------------- |
| createProject | String name, String key, String description             | Creates a new project. Owner ID from security context. Returns project ID. |
| getProject    | Long id                                              | Returns a project by ID.                                                   |
| listProjects  | —                                                       | Returns all projects.                                                      |
| updateProject | Long id, String name, String description             | Updates project name and description. Updates `updatedAt`.                 |
| deleteProject | Long id                                              | Deletes project by ID.                                                     |
| addMember     | Long projectId, Long userId, ProjectRoleType role | Adds a member with role.                                                   |
| getMembers    | Long projectId                                       | Returns project members.                                                   |
| removeMember  | Long projectId, Long userId                       | Removes a member.                                                          |

### IssueService

| Method        | Parameters                                                                             | Description                                                    |
| ------------- | -------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| createIssue   | Long projectId, String title, String description, IssueType type, Priority priority | Creates an issue. Default status ID=1. Adds to `IssueHistory`. |
| getIssue      | Long id                                                                             | Returns an issue by ID.                                        |
| listIssues    | Long projectId                                                                      | Returns all issues in a project.                               |
| updateIssue   | Long id, String title, String description                                           | Updates title/description. Adds to `IssueHistory`.             |
| deleteIssue   | Long id                                                                             | Deletes an issue.                                              |
| patchStatus   | Long id, Long newStatusId                                                        | Updates status. Adds history entry.                            |
| patchAssignee | Long id, Long assigneeId                                                         | Changes assignee. Adds history entry.                          |
| createStatus  | Long projectId, String name, StatusCategory category                                | Creates a status. Returns status ID.                           |
| getStatuses   | Long projectId                                                                      | Returns all statuses for a project.                            |
| updateStatus  | Long id, String name                                                                | Updates status name.                                           |
| deleteStatus  | Long id                                                                             | Deletes a status.                                              |
| getHistory    | Long issueId                                                                        | Returns all `IssueHistory` entries.                            |

### DetailsService (Comments, Attachments, Labels)

| Method        | Parameters                                | Description                           |
| ------------- |-------------------------------------------| ------------------------------------- |
| addComment    | Long issueId, String content, Long userId | Adds a comment.                       |
| getComments   | Long issueId                              | Returns all comments for an issue.    |
| updateComment | Long id, String content                   | Updates comment and sets `updatedAt`. |
| deleteComment | Long id                                   | Deletes a comment.                    |


| Method           | Parameters                                                                   | Description          |
| ---------------- |------------------------------------------------------------------------------| -------------------- |
| addAttachment    | Long issueId, String fileName, String fileUrl, Integer fileSize, Long userId | Adds attachment.     |
| getAttachments   | Long issueId                                                                 | Returns attachments. |
| deleteAttachment | Long id                                                                      | Deletes attachment.  |


| Method               | Parameters                       | Description                       |
| -------------------- | -------------------------------- | --------------------------------- |
| createLabel          | String name, String color        | Creates label. Returns label ID.  |
| getLabels            | —                                | Returns all labels.               |
| addLabelToIssue      | Long issueId, Long labelId | Assigns label to issue.           |
| removeLabelFromIssue | Long issueId, Long labelId | Removes label from issue.         |
| getLabelsForIssue    | Long issueId                  | Returns labels assigned to issue. |

