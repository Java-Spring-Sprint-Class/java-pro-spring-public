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
- Integer userId
- ProjectRoleType role

---

### Issue DTOs
**CreateIssueRequest**
- Integer projectId
- String title
- String description
- IssueType type
- Priority priority

**UpdateIssueRequest**
- String title
- String description

**PatchStatusRequest**
- Integer statusId

**PatchAssigneeRequest**
- Integer assigneeId

---

### Status DTOs
**CreateStatusRequest**
- Integer projectId
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
- getUser(Integer id)
- listUsers(String search)
- updateProfile(Integer id, UpdateProfileRequest request)
- deactivateUser(Integer id)
- assignRole(Integer userId, Integer roleId)
- removeRole(Integer userId, Integer roleId)

---

### RoleController (`/api/roles`)
- createRole(CreateRoleRequest request)
- getRoles()

---

### ProjectController (`/api/projects`)
- createProject(CreateProjectRequest request)
- getProject(Integer id)
- listProjects()
- updateProject(Integer id, UpdateProjectRequest request)
- deleteProject(Integer id)
- addMember(Integer projectId, AddMemberRequest request)
- getProjectMembers(Integer projectId)
- removeMember(Integer projectId, Integer userId)

---

### IssueController (`/api/issues`)
- createIssue(CreateIssueRequest request)
- getIssue(Integer id)
- listIssues(Integer projectId)
- updateIssue(Integer id, UpdateIssueRequest request)
- deleteIssue(Integer id)
- updateStatus(Integer id, PatchStatusRequest request)
- updateAssignee(Integer id, PatchAssigneeRequest request)
- getHistory(Integer id)

---

### StatusController (`/api/statuses`)
- createStatus(CreateStatusRequest request)
- getStatuses(Integer projectId)
- updateStatus(Integer id, UpdateStatusRequest request)
- deleteStatus(Integer id)

---

### CommentController (`/api`)
- addComment(Integer issueId, AddCommentRequest request)
- getComments(Integer issueId)
- updateComment(Integer commentId, UpdateCommentRequest request)
- deleteComment(Integer commentId)

---

### AttachmentController (`/api`)
- addAttachment(Integer issueId, AddAttachmentRequest request)
- getAttachments(Integer issueId)
- deleteAttachment(Integer attachmentId)

---

### LabelController (`/api`)
- createLabel(CreateLabelRequest request)
- getAllLabels()
- addLabelToIssue(Integer issueId, Integer labelId)
- getLabelsForIssue(Integer issueId)
- removeLabelFromIssue(Integer issueId, Integer labelId)  

