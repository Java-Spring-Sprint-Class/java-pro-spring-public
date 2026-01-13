# AgileNext Junior Java Developer Mission

Welcome to **AgileNext Junior Java Developer Mission**.

You are the new **Junior Java Developer** at the startup AgileNext. Our Tech Lead left a prototype of the core system of our future project management tool (a Jira-like application).

Your mission: transform this prototype into a full-fledged, working **REST API application**. Ensure the system is reliable, the code is clean, and tests are green.

---

## Entities

### 1. User

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
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
| id | Integer | `id` |
| name | String | `name` |

### 3. UserRole (Many-to-Many)

| Field | Type | Variable Name |
|-------|------|---------------|
| userId | Integer | `userId` |
| roleId | Integer | `roleId` |

### 4. Project

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| name | String | `name` |
| key | String | `key` |
| ownerId | Integer | `ownerId` |
| description | String | `description` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 5. ProjectMember

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| projectId | Integer | `projectId` |
| userId | Integer | `userId` |
| role | ProjectRoleType | `role` |

### 6. Issue

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| projectId | Integer | `projectId` |
| key | String | `key` |
| title | String | `title` |
| description | String | `description` |
| type | IssueType | `type` |
| priority | Priority | `priority` |
| statusId | Integer | `statusId` |
| assigneeId | Integer | `assigneeId` |
| reporterId | Integer | `reporterId` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 7. Status

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| name | String | `name` |
| category | StatusCategory | `category` |
| position | Integer | `position` |
| projectId | Integer | `projectId` |

### 8. IssueComment

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| issueId | Integer | `issueId` |
| userId | Integer | `userId` |
| content | String | `content` |
| createdAt | LocalDateTime | `createdAt` |
| updatedAt | LocalDateTime | `updatedAt` |

### 9. Attachment

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| issueId | Integer | `issueId` |
| userId | Integer | `userId` |
| fileName | String | `fileName` |
| fileUrl | String | `fileUrl` |
| fileSize | Integer | `fileSize` |
| createdAt | LocalDateTime | `createdAt` |

### 10. IssueHistory

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| issueId | Integer | `issueId` |
| userId | Integer | `userId` |
| fieldChanged | String | `fieldChanged` |
| oldValue | String | `oldValue` |
| newValue | String | `newValue` |
| createdAt | LocalDateTime | `createdAt` |

### 11. Label

| Field | Type | Variable Name |
|-------|------|---------------|
| id | Integer | `id` |
| name | String | `name` |
| color | String | `color` |

### 12. IssueLabel (Many-to-Many)

| Field | Type | Variable Name |
|-------|------|---------------|
| issueId | Integer | `issueId` |
| labelId | Integer | `labelId` |

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
| getUser        | Integer id                                     | Retrieves a user by ID.                                |
| listUsers      | String search                                  | Returns a list of users. Filters by username or email. |
| updateProfile  | Integer id, String username, String email      | Updates username and email. Updates `updatedAt`.       |
| deactivateUser | Integer id                                     | Deactivates user (`isActive=false`).                   |
| assignRole     | Integer userId, Integer roleId                 | Assigns role to user.                                  |
| removeRole     | Integer userId, Integer roleId                 | Removes role from user.                                |

### RoleService

| Method     | Parameters  | Description                          |
| ---------- | ----------- | ------------------------------------ |
| createRole | String name | Creates a new role. Returns role ID. |
| getRoles   | —           | Returns all roles.                   |

### ProjectService

| Method        | Parameters                                              | Description                                                                |
| ------------- | ------------------------------------------------------- | -------------------------------------------------------------------------- |
| createProject | String name, String key, String description             | Creates a new project. Owner ID from security context. Returns project ID. |
| getProject    | Integer id                                              | Returns a project by ID.                                                   |
| listProjects  | —                                                       | Returns all projects.                                                      |
| updateProject | Integer id, String name, String description             | Updates project name and description. Updates `updatedAt`.                 |
| deleteProject | Integer id                                              | Deletes project by ID.                                                     |
| addMember     | Integer projectId, Integer userId, ProjectRoleType role | Adds a member with role.                                                   |
| getMembers    | Integer projectId                                       | Returns project members.                                                   |
| removeMember  | Integer projectId, Integer userId                       | Removes a member.                                                          |

### IssueService

| Method        | Parameters                                                                             | Description                                                    |
| ------------- | -------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| createIssue   | Integer projectId, String title, String description, IssueType type, Priority priority | Creates an issue. Default status ID=1. Adds to `IssueHistory`. |
| getIssue      | Integer id                                                                             | Returns an issue by ID.                                        |
| listIssues    | Integer projectId                                                                      | Returns all issues in a project.                               |
| updateIssue   | Integer id, String title, String description                                           | Updates title/description. Adds to `IssueHistory`.             |
| deleteIssue   | Integer id                                                                             | Deletes an issue.                                              |
| patchStatus   | Integer id, Integer newStatusId                                                        | Updates status. Adds history entry.                            |
| patchAssignee | Integer id, Integer assigneeId                                                         | Changes assignee. Adds history entry.                          |
| createStatus  | Integer projectId, String name, StatusCategory category                                | Creates a status. Returns status ID.                           |
| getStatuses   | Integer projectId                                                                      | Returns all statuses for a project.                            |
| updateStatus  | Integer id, String name                                                                | Updates status name.                                           |
| deleteStatus  | Integer id                                                                             | Deletes a status.                                              |
| getHistory    | Integer issueId                                                                        | Returns all `IssueHistory` entries.                            |

### DetailsService (Comments, Attachments, Labels)

| Method        | Parameters                      | Description                           |
| ------------- | ------------------------------- | ------------------------------------- |
| addComment    | Integer issueId, String content | Adds a comment.                       |
| getComments   | Integer issueId                 | Returns all comments for an issue.    |
| updateComment | Integer id, String content      | Updates comment and sets `updatedAt`. |
| deleteComment | Integer id                      | Deletes a comment.                    |


| Method           | Parameters                                                         | Description          |
| ---------------- | ------------------------------------------------------------------ | -------------------- |
| addAttachment    | Integer issueId, String fileName, String fileUrl, Integer fileSize | Adds attachment.     |
| getAttachments   | Integer issueId                                                    | Returns attachments. |
| deleteAttachment | Integer id                                                         | Deletes attachment.  |


| Method               | Parameters                       | Description                       |
| -------------------- | -------------------------------- | --------------------------------- |
| createLabel          | String name, String color        | Creates label. Returns label ID.  |
| getLabels            | —                                | Returns all labels.               |
| addLabelToIssue      | Integer issueId, Integer labelId | Assigns label to issue.           |
| removeLabelFromIssue | Integer issueId, Integer labelId | Removes label from issue.         |
| getLabelsForIssue    | Integer issueId                  | Returns labels assigned to issue. |

