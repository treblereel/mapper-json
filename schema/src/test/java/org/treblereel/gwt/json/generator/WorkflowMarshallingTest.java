/*
 * Copyright © 2026 Treblereel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.treblereel.gwt.json.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.treblereel.gwt.json.generator.workflow.*;

class WorkflowMarshallingTest {

  @Test
  void schemaRoundTrip() {
    Schema schema = new Schema();
    schema.setFormat("json");

    String json = Schema_JsonMapperImpl.INSTANCE.toJSON(schema);
    assertTrue(json.contains("\"format\":\"json\""));

    Schema result = Schema_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals("json", result.getFormat());
  }

  @Test
  void inputWithNestedSchemaRoundTrip() {
    Schema schema = new Schema();
    schema.setFormat("yaml");

    Input input = new Input();
    input.setSchema(schema);

    String json = Input_JsonMapperImpl.INSTANCE.toJSON(input);
    assertTrue(json.contains("\"format\":\"yaml\""));

    Input result = Input_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertNotNull(result.getSchema());
    assertEquals("yaml", result.getSchema().getFormat());
  }

  @Test
  void externalResourceRoundTrip() {
    ExternalResource resource = new ExternalResource();
    resource.setName("my-api");

    String json = ExternalResource_JsonMapperImpl.INSTANCE.toJSON(resource);
    assertTrue(json.contains("\"name\":\"my-api\""));

    ExternalResource result = ExternalResource_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals("my-api", result.getName());
  }

  @Test
  void eventPropertiesRoundTrip() {
    EventProperties props = new EventProperties();
    props.setId("evt-001");
    props.setType("com.example.event");
    props.setSubject("test-subject");
    props.setDatacontenttype("application/json");

    String json = EventProperties_JsonMapperImpl.INSTANCE.toJSON(props);
    assertTrue(json.contains("\"id\":\"evt-001\""));
    assertTrue(json.contains("\"type\":\"com.example.event\""));

    EventProperties result = EventProperties_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals("evt-001", result.getId());
    assertEquals("com.example.event", result.getType());
    assertEquals("test-subject", result.getSubject());
    assertEquals("application/json", result.getDatacontenttype());
  }

  @Test
  void oauth2WithEnumAndListsRoundTrip() {
    Oauth2AuthenticationProperties oauth = new Oauth2AuthenticationProperties();
    oauth.setGrant(Grant.CLIENT_CREDENTIALS);
    oauth.setUsername("user1");
    oauth.setPassword("secret");
    oauth.setScopes(List.of("read", "write"));
    oauth.setAudiences(List.of("api.example.com"));
    oauth.setIssuers(List.of("https://auth.example.com"));

    Oauth2Token subject = new Oauth2Token();
    subject.setToken("subj-token");
    subject.setType("urn:ietf:params:oauth:token-type:jwt");
    oauth.setSubject(subject);

    String json = Oauth2AuthenticationProperties_JsonMapperImpl.INSTANCE.toJSON(oauth);
    assertTrue(json.contains("\"grant\":\"CLIENT_CREDENTIALS\""));
    assertTrue(json.contains("\"username\":\"user1\""));
    assertTrue(json.contains("\"scopes\":[\"read\",\"write\"]"));
    assertTrue(json.contains("\"token\":\"subj-token\""));

    Oauth2AuthenticationProperties result =
        Oauth2AuthenticationProperties_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals(Grant.CLIENT_CREDENTIALS, result.getGrant());
    assertEquals("user1", result.getUsername());
    assertEquals(List.of("read", "write"), result.getScopes());
    assertNotNull(result.getSubject());
    assertEquals("subj-token", result.getSubject().getToken());
  }

  @Test
  void retryPolicyRoundTrip() {
    RetryPolicy policy = new RetryPolicy();
    policy.setWhen("error");
    policy.setExceptWhen("timeout");

    String json = RetryPolicy_JsonMapperImpl.INSTANCE.toJSON(policy);
    assertTrue(json.contains("\"when\":\"error\""));
    assertTrue(json.contains("\"exceptWhen\":\"timeout\""));

    RetryPolicy result = RetryPolicy_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals("error", result.getWhen());
    assertEquals("timeout", result.getExceptWhen());
  }

  @Test
  void errorWithStatusRoundTrip() {
    org.treblereel.gwt.json.generator.workflow.Error error =
        new org.treblereel.gwt.json.generator.workflow.Error();
    error.setStatus(404);

    String json = Error_JsonMapperImpl.INSTANCE.toJSON(error);
    assertTrue(json.contains("\"status\":404"));

    org.treblereel.gwt.json.generator.workflow.Error result =
        Error_JsonMapperImpl.INSTANCE.fromJSON(json);
    assertEquals(404, result.getStatus());
  }

  @Test
  void rootWithInputOutputRoundTrip() {
    Schema inputSchema = new Schema();
    inputSchema.setFormat("json");
    Input input = new Input();
    input.setSchema(inputSchema);

    Schema outputSchema = new Schema();
    outputSchema.setFormat("yaml");
    Output output = new Output();
    output.setSchema(outputSchema);

    Root root = new Root();
    root.setInput(input);
    root.setOutput(output);

    String json = Root_JsonMapperImpl.INSTANCE.toJSON(root);
    Root result = Root_JsonMapperImpl.INSTANCE.fromJSON(json);

    assertNotNull(result.getInput());
    assertEquals("json", result.getInput().getSchema().getFormat());
    assertNotNull(result.getOutput());
    assertEquals("yaml", result.getOutput().getSchema().getFormat());
  }

  @Test
  void grantEnumValues() {
    assertNotNull(Grant.AUTHORIZATION_CODE);
    assertNotNull(Grant.CLIENT_CREDENTIALS);
    assertNotNull(Grant.PASSWORD);
    assertNotNull(Grant.REFRESH_TOKEN);
    assertNotNull(Grant.URN_IETF_PARAMS_OAUTH_GRANT_TYPE_TOKEN_EXCHANGE);
  }

  @Test
  void jsonRoundTripPreservesAllFields() {
    Oauth2Token token = new Oauth2Token();
    token.setToken("abc123");
    token.setType("bearer");

    String json = Oauth2Token_JsonMapperImpl.INSTANCE.toJSON(token);
    Oauth2Token result = Oauth2Token_JsonMapperImpl.INSTANCE.fromJSON(json);
    String json2 = Oauth2Token_JsonMapperImpl.INSTANCE.toJSON(result);

    assertEquals(json, json2);
  }
}
