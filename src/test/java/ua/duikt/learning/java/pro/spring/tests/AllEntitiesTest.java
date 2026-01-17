package ua.duikt.learning.java.pro.spring.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.*;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */

class AllEntitiesTest {

    @Test
    @DisplayName("User Entity: Check fields and Builder")
    void userEntityTest() {
        assertFieldExists(User.class, "username", String.class);
        assertFieldExists(User.class, "passwordHash", String.class);
        assertFieldExists(User.class, "isActive", Boolean.class);

        User user = User.builder().username("testUser").isActive(true).build();
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Role Entity: Check fields")
    void roleEntityTest() {
        assertFieldExists(Role.class, "name", String.class);

        Role role = Role.builder().id(1L).name("ADMIN").build();
        assertThat(role.getName()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("UserRole Entity: Check Many-to-Many fields")
    void userRoleEntityTest() {
        assertFieldExists(UserRole.class, "userId", Long.class);
        assertFieldExists(UserRole.class, "roleId", Long.class);

        UserRole ur = UserRole.builder().userId(1L).roleId(2L).build();
        assertThat(ur.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Project Entity: Check fields")
    void projectEntityTest() {
        assertFieldExists(Project.class, "name", String.class);
        assertFieldExists(Project.class, "projectKey", String.class);
        assertFieldExists(Project.class, "ownerId", Long.class);

        Project project = Project.builder().projectKey("PRJ").ownerId(10L).build();
        assertThat(project.getProjectKey()).isEqualTo("PRJ");
    }

    @Test
    @DisplayName("ProjectMember Entity: Check Enum usage")
    void projectMemberEntityTest() {
        assertFieldExists(ProjectMember.class, "projectId", Long.class);
        assertFieldExists(ProjectMember.class, "userId", Long.class);
        assertFieldExists(ProjectMember.class, "role", ProjectRoleType.class);

        ProjectMember pm = ProjectMember.builder().role(ProjectRoleType.OWNER).build();
        assertThat(pm.getRole()).isEqualTo(ProjectRoleType.OWNER);
    }

    @Test
    @DisplayName("Status Entity: Check Enum usage")
    void statusEntityTest() {
        assertFieldExists(Status.class, "name", String.class);
        assertFieldExists(Status.class, "category", StatusCategory.class);
        assertFieldExists(Status.class, "position", Integer.class);
        assertFieldExists(Status.class, "projectId", Long.class);

        Status status = Status.builder().category(StatusCategory.DONE).build();
        assertThat(status.getCategory()).isEqualTo(StatusCategory.DONE);
    }

    @Test
    @DisplayName("Issue Entity: Check comprehensive fields and Enums")
    void issueEntityTest() {
        assertFieldExists(Issue.class, "type", IssueType.class);
        assertFieldExists(Issue.class, "priority", Priority.class);
        assertFieldExists(Issue.class, "projectId", Long.class);
        assertFieldExists(Issue.class, "assigneeId", Long.class);
        assertFieldExists(Issue.class, "reporterId", Long.class);
        assertFieldExists(Issue.class, "issueKey", String.class);

        Issue issue = Issue.builder()
                .type(IssueType.STORY)
                .priority(Priority.MEDIUM)
                .build();

        assertThat(issue.getType()).isEqualTo(IssueType.STORY);
        assertThat(issue.getPriority()).isEqualTo(Priority.MEDIUM);
    }

    @Test
    @DisplayName("IssueComment Entity: Check fields")
    void issueCommentTest() {
        assertFieldExists(IssueComment.class, "issueId", Long.class);
        assertFieldExists(IssueComment.class, "content", String.class);
        assertFieldExists(IssueComment.class, "createdAt", LocalDateTime.class);

        IssueComment comment = IssueComment.builder().content("LGTM").build();
        assertThat(comment.getContent()).isEqualTo("LGTM");
    }

    @Test
    @DisplayName("Attachment Entity: Check fields")
    void attachmentTest() {
        assertFieldExists(Attachment.class, "fileName", String.class);
        assertFieldExists(Attachment.class, "fileUrl", String.class);
        assertFieldExists(Attachment.class, "fileSize", Integer.class);

        Attachment att = Attachment.builder().fileName("log.txt").fileSize(1024).build();
        assertThat(att.getFileSize()).isEqualTo(1024);
    }

    @Test
    @DisplayName("IssueHistory Entity: Check fields")
    void issueHistoryTest() {
        assertFieldExists(IssueHistory.class, "fieldChanged", String.class);
        assertFieldExists(IssueHistory.class, "oldValue", String.class);
        assertFieldExists(IssueHistory.class, "newValue", String.class);

        IssueHistory hist = IssueHistory.builder().fieldChanged("status").build();
        assertThat(hist.getFieldChanged()).isEqualTo("status");
    }

    @Test
    @DisplayName("Label & IssueLabel Entities: Check fields")
    void labelEntitiesTest() {
        assertFieldExists(Label.class, "name", String.class);
        assertFieldExists(Label.class, "color", String.class);

        Label label = Label.builder().color("#FFF").build();
        assertThat(label.getColor()).isEqualTo("#FFF");

        assertFieldExists(IssueLabel.class, "issueId", Long.class);
        assertFieldExists(IssueLabel.class, "labelId", Long.class);
    }

    /**
     * Reflection helper to ensure the class explicitly declares a field with the specific type.
     */
    private void assertFieldExists(Class<?> clazz, String fieldName, Class<?> expectedType) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            assertThat(field.getType())
                    .as("Field '%s' in class '%s' must be of type %s",
                            fieldName, clazz.getSimpleName(), expectedType.getSimpleName())
                    .isEqualTo(expectedType);
        } catch (NoSuchFieldException e) {
            List<String> actualFields = Arrays.stream(clazz.getDeclaredFields())
                    .map(Field::getName)
                    .toList();
            throw new AssertionError(String.format(
                    "Field '%s' missing in %s. Found: %s",
                    fieldName, clazz.getSimpleName(), actualFields
            ));
        }
    }
}
