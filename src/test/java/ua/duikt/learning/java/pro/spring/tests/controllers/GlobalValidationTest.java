package ua.duikt.learning.java.pro.spring.tests.controllers;

import org.junit.jupiter.api.DisplayName;
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
class GlobalValidationTest {}
