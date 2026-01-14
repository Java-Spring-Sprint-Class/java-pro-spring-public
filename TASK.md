# Task: Define API Endpoints Contract

## Goal


### User DTOs
**RegisterRequest**
- String username
- String email
- String password

**UpdateProfileRequest**
- String username
- String email

---

### Role DTOs
**CreateRoleRequest**
- String name

---

### Project DTOs
**CreateProjectRequest**
- String name
- String key
- String description

**UpdateProjectRequest**
- String name
- String description

**AddMemberRequest**
- Long userId
- ProjectRoleType role

---

### Issue DTOs
**CreateIssueRequest**
- Long projectId
- String title
- String description
- IssueType type
- Priority priority
- Long statusId

**UpdateIssueRequest**
- String title
- String description

**PatchStatusRequest**
- Long statusId

**PatchAssigneeRequest**
- Long assigneeId

---

### Status DTOs
**CreateStatusRequest**
- Long projectId
- String name
- StatusCategory category

**UpdateStatusRequest**
- String name

---

### Comment DTOs
**AddCommentRequest**
- String content

**UpdateCommentRequest**
- String content

---

### Attachment DTOs
**AddAttachmentRequest**
- String fileName
- String fileUrl
- Integer fileSize

---

### Label DTOs
**CreateLabelRequest**
- String name
- String color

---

## 2. API Endpoints Contract


---

### UserController (`/api/users`)
- register(RegisterRequest request)
- getUser(Long id)
- listUsers(String search)
- updateProfile(Long id, UpdateProfileRequest request)
- deactivateUser(Long id)
- assignRole(Long userId, Long roleId)
- removeRole(Long userId, Long roleId)

---

### RoleController (`/api/roles`)
- createRole(CreateRoleRequest request)
- getRoles()

---

### ProjectController (`/api/projects`)
- createProject(CreateProjectRequest request)
- getProject(Long id)
- listProjects()
- updateProject(Long id, UpdateProjectRequest request)
- deleteProject(Long id)
- addMember(Long projectId, AddMemberRequest request)
- getProjectMembers(Long projectId)
- removeMember(Long projectId, Long userId)

---

### IssueController (`/api/issues`)
- createIssue(CreateIssueRequest request)
- getIssue(Long id)
- listIssues(Long projectId)
- updateIssue(Long id, UpdateIssueRequest request)
- deleteIssue(Long id)
- updateStatus(Long id, PatchStatusRequest request)
- updateAssignee(Long id, PatchAssigneeRequest request)
- getHistory(Long id)

---

### StatusController (`/api/statuses`)
- createStatus(CreateStatusRequest request)
- getStatuses(Long projectId)
- updateStatus(Long id, UpdateStatusRequest request)
- deleteStatus(Long id)

---

### CommentController (`/api`)
- addComment(Long issueId, AddCommentRequest request)
- getComments(Long issueId)
- updateComment(Long commentId, UpdateCommentRequest request)
- deleteComment(Long commentId)

---

### AttachmentController (`/api`)
- addAttachment(Long issueId, AddAttachmentRequest request)
- getAttachments(Long issueId)
- deleteAttachment(Long attachmentId)

---

### LabelController (`/api`)
- createLabel(CreateLabelRequest request)
- getAllLabels()
- addLabelToIssue(Long issueId, Long labelId)
- getLabelsForIssue(Long issueId)
- removeLabelFromIssue(Long issueId, Long labelId)  

