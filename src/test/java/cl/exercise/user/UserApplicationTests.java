package cl.exercise.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cl.exercise.user.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest
@ActiveProfiles("test")
class UserApplicationTests {

  @Autowired
  UserController controller;

  @Test
  void contextLoads() {
    assertNotNull(controller);
  }
}
