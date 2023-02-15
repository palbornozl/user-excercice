package cl.exercise.user.controller;

import static cl.exercise.user.utils.Utils.mapFromJson;
import static cl.exercise.user.utils.Utils.mapToJson;
import static cl.exercise.user.utils.Utils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import cl.exercise.user.dto.UserDTO;
import cl.exercise.user.dto.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {
  protected MockMvc mvc;
  File file;
  @Autowired WebApplicationContext webApplicationContext;
  private UserDTO userDTOSignUpRequest;
  private UserDTO userDTOSignInRequest;
  private UserDTO userDTOUpdateRequest;

  @Before
  public void setUp() throws IOException {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    ClassLoader classLoader = getClass().getClassLoader();

    JsonNode jsonNode = readFile("signUpRequest", classLoader);
    userDTOSignUpRequest = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserDTO.class);
    userDTOSignUpRequest.setUserToken(jsonNode.get("password").asText());

    jsonNode = readFile("signInRequest", classLoader);
    userDTOSignInRequest = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserDTO.class);

    jsonNode = readFile("updateUserRequest", classLoader);
    userDTOUpdateRequest = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserDTO.class);
  }

  @Test
  public void shouldSignUp() throws Exception {
    String uri = "/sign-up";

    String inputJson = mapToJson(userDTOSignInRequest);
    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(
                        "Authorization",
                        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlem9yZy5jbCIsImV4cCI6MTYwMTc3OTUwNX0.5Lm353B6_9rpgNd19am7lky9WMcmvDtictPlbWmcvJcUoGD7nQik-Dz_uXb_ymnVq_F7Z_2BV8GmXitQUotEcQ")
                    .content(inputJson))
            .andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);
    String content = mvcResult.getResponse().getContentAsString();
    UserResponse response = mapFromJson(content, UserResponse.class);
    assertFalse(ObjectUtils.isEmpty(response), "Response found: " + response.toString());
  }

  @Test
  public void shouldUpdate() throws Exception {
    String uri = "/in/update";

    String inputJson = mapToJson(userDTOUpdateRequest);
    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(
                        "Authorization",
                        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlem9yZy5jbCIsImV4cCI6MTYwMTc3OTUwNX0.5Lm353B6_9rpgNd19am7lky9WMcmvDtictPlbWmcvJcUoGD7nQik-Dz_uXb_ymnVq_F7Z_2BV8GmXitQUotEcQ")
                    .content(inputJson))
            .andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);
    String content = mvcResult.getResponse().getContentAsString();
    UserResponse response = mapFromJson(content, UserResponse.class);
    assertFalse(ObjectUtils.isEmpty(response), "Response found: " + response.toString());
  }

  @Test
  public void shouldSignIn() throws Exception {
    String uri = "/in/sign-in";

    String inputJson = mapToJson(userDTOSignInRequest);
    MvcResult mvcResult =
        mvc.perform(
                MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(inputJson))
            .andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);
    String content = mvcResult.getResponse().getContentAsString();
    UserResponse response = mapFromJson(content, UserResponse.class);
    assertFalse(ObjectUtils.isEmpty(response), "Response found: " + response.toString());
  }
}
