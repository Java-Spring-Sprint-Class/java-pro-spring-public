package ua.duikt.learning.java.pro.spring.tests.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ua.duikt.learning.java.pro.spring.controllers.*;

/**
 * Created by Mykyta Sirobaba on 16.01.2026.
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
// TODO: Implements GlobalValidationTest
@DisplayName("Global Validation Bad Flow Test")
class GlobalValidationTest {
    // TODO
    @Test
    @DisplayName("Attachment: Should fail when filename is empty or file size is invalid")
    void addAttachment_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Comment: Should fail when content is empty or too long")
    void updateComment_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Issue: Create - Should fail when required fields are null")
    void createIssue_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Issue: Update - Should fail when title is empty")
    void updateIssue_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Issue: Patch Status - Should fail when statusId is null")
    void patchIssueStatus_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Issue: Patch Assignee - Should fail when assigneeId is null")
    void patchIssueAssignee_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Label: Should fail when HEX color format is invalid")
    void createLabel_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Project: Create - Should fail when name is blank or key too short")
    void createProject_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Project: Add Member - Should fail when role is null")
    void addProjectMember_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Role: Should fail when name is blank")
    void createRole_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Status: Create - Should fail when category is missing")
    void createStatus_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("Status: Update - Should fail when name is empty")
    void updateStatus_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("User: Register - Should fail when email is invalid or password too short")
    void registerUser_BadFlow() throws Exception {}

    // TODO
    @Test
    @DisplayName("User: Update Profile - Should fail when username is empty")
    void updateProfile_BadFlow() throws Exception {}
}
