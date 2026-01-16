package ua.duikt.learning.java.pro.spring.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.*;
import ua.duikt.learning.java.pro.spring.dtos.*;
import ua.duikt.learning.java.pro.spring.service.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest({
        AttachmentController.class,
        CommentController.class,
        IssueController.class,
        LabelController.class,
        ProjectController.class,
        RoleController.class,
        StatusController.class,
        UserController.class
})
@DisplayName("Global Validation Bad Flow Test")
class GlobalValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AttachmentService attachmentService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private IssueService issueService;
    @MockitoBean
    private LabelService labelService;
    @MockitoBean
    private ProjectService projectService;
    @MockitoBean
    private RoleService roleService;
    @MockitoBean
    private StatusService statusService;
    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Attachment: Should fail when filename is empty or file size is invalid")
    void addAttachment_BadFlow() throws Exception {
        AddAttachmentRequest invalidRequest = AddAttachmentRequest.builder()
                .fileName("")
                .fileUrl("")
                .fileSize(-100)
                .build();

        mockMvc.perform(post("/api/user/1/issues/1/attachments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Comment: Should fail when content is empty or too long")
    void updateComment_BadFlow() throws Exception {
        UpdateCommentRequest invalidRequest = UpdateCommentRequest.builder()
                .content("")
                .build();

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Issue: Create - Should fail when required fields are null")
    void createIssue_BadFlow() throws Exception {
        CreateIssueRequest invalidRequest = CreateIssueRequest.builder()
                .projectId(null)
                .title("")
                .type(null)
                .build();

        mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Issue: Update - Should fail when title is empty")
    void updateIssue_BadFlow() throws Exception {
        UpdateIssueRequest invalidRequest = UpdateIssueRequest.builder()
                .title("")
                .description("Desc")
                .build();

        mockMvc.perform(put("/api/issues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Issue: Patch Status - Should fail when statusId is null")
    void patchIssueStatus_BadFlow() throws Exception {
        PatchStatusRequest invalidRequest = PatchStatusRequest.builder()
                .statusId(null)
                .build();

        mockMvc.perform(patch("/api/issues/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Issue: Patch Assignee - Should fail when assigneeId is null")
    void patchIssueAssignee_BadFlow() throws Exception {
        PatchAssigneeRequest invalidRequest = PatchAssigneeRequest.builder()
                .assigneeId(null)
                .build();

        mockMvc.perform(patch("/api/issues/1/assignee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Label: Should fail when HEX color format is invalid")
    void createLabel_BadFlow() throws Exception {
        CreateLabelRequest invalidRequest = CreateLabelRequest.builder()
                .name("Bug")
                .color("ZZZZZZ")
                .build();

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Project: Create - Should fail when name is blank or key too short")
    void createProject_BadFlow() throws Exception {
        CreateProjectRequest invalidRequest = CreateProjectRequest.builder()
                .name("")
                .key("A")
                .userId(1L)
                .build();

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Project: Add Member - Should fail when role is null")
    void addProjectMember_BadFlow() throws Exception {
        AddMemberRequest invalidRequest = AddMemberRequest.builder()
                .userId(1L)
                .role(null)
                .build();

        mockMvc.perform(post("/api/projects/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Role: Should fail when name is blank")
    void createRole_BadFlow() throws Exception {
        CreateRoleRequest invalidRequest = CreateRoleRequest.builder()
                .name("   ")
                .build();

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Status: Create - Should fail when category is missing")
    void createStatus_BadFlow() throws Exception {
        CreateStatusRequest invalidRequest = CreateStatusRequest.builder()
                .projectId(1L)
                .name("New Status")
                .category(null)
                .build();

        mockMvc.perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Status: Update - Should fail when name is empty")
    void updateStatus_BadFlow() throws Exception {
        UpdateStatusRequest invalidRequest = UpdateStatusRequest.builder()
                .name("")
                .build();

        mockMvc.perform(put("/api/statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User: Register - Should fail when email is invalid or password too short")
    void registerUser_BadFlow() throws Exception {
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .username("User")
                .email("not-an-email")
                .password("123")
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User: Update Profile - Should fail when username is empty")
    void updateProfile_BadFlow() throws Exception {
        UpdateProfileRequest invalidRequest = UpdateProfileRequest.builder()
                .username("")
                .email("valid@email.com")
                .build();

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}