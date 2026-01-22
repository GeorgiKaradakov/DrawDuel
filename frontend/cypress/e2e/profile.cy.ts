describe("User Settings E2E", () => {
  const TEST_USER = {
    username: `cypressUser${Date.now()}`,
    email: `cypress_${Date.now()}@settings.com`,
    password: "Password123!",
  };

  before(() => {
    cy.register(TEST_USER.username, TEST_USER.email, TEST_USER.password);
  });

  beforeEach(() => {
    cy.session(TEST_USER.email, () => {
      cy.login(TEST_USER.email, TEST_USER.password);
    });

    cy.visit("/dashboard");
  });

  it("loads the User Settings page", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.contains("Account").should("be.visible");
    cy.contains("Security").should("be.visible");
    cy.contains("Devices").should("be.visible");
  });

  it("updates username successfully", () => {
    const newUsername = `user_${Date.now()}`;

    cy.get('[data-cy="navbar-AccountSettings"]').click();
    cy.scrollTo("right");

    cy.intercept("PATCH", "/api/user/update-username").as("updateUsername");

    cy.get('[data-cy="change-Username-input"]').clear().type(newUsername);
    cy.get('[data-cy="change-Username-submit"]').click();

    cy.wait("@updateUsername").its("response.statusCode").should("eq", 200);

    cy.contains("Username updated successfully!").should("be.visible");
  });

  it("updates profile image successfully", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.intercept("POST", "/api/user/update-profile-image").as(
      "uploadProfileImage",
    );

    cy.get('[data-cy="change-profile-image-button"]').click();

    cy.get('[data-cy="change-profile-image-input"]').selectFile(
      "cypress/fixtures/test-profile-image.png",
      { force: true },
    );

    cy.wait("@uploadProfileImage").its("response.statusCode").should("eq", 200);

    cy.contains("Profile image updated successfully!").should("be.visible");
  });

  it("shows active devices table", () => {
    cy.intercept("GET", "/api/user/get-devices").as("getDevices");

    cy.get('[data-cy="navbar-AccountSettings"]').click();
    cy.get('[data-cy="settings-sidebar-device-management"]').click();

    cy.wait("@getDevices").its("response.statusCode").should("eq", 200);

    cy.get("table").within(() => {
      cy.contains("Operating System").should("be.visible");
      cy.contains("Browser").should("be.visible");
      cy.contains("Status").should("be.visible");
    });
  });

  it("does not allow revoking current session", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();
    cy.get('[data-cy="settings-sidebar-device-management"]').click();

    cy.intercept("DELETE", "/api/user/revoke-session/*").as("revokeSession");

    cy.get('[data-cy="device-row"]')
      .first()
      .within(() => {
        cy.get('[data-cy="revoke-session-button-current-session"]').click();
      });

    cy.wait("@revokeSession")
      .its("response.statusCode")
      .should("be.oneOf", [400, 409]);
  });

  it("removes profile image successfully", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.intercept("DELETE", "/api/user/delete-profile-image").as(
      "deleteProfileImage",
    );

    cy.get('[data-cy="remove-profile-image-button"]').click();

    cy.wait("@deleteProfileImage").its("response.statusCode").should("eq", 200);

    cy.contains("Profile image removed").should("be.visible");
  });

  it("deletes account successfully", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.intercept("DELETE", "/api/user/delete-account").as("deleteAccount");

    cy.get('[data-cy="delete-profile-button"]').click();

    cy.wait("@deleteAccount").its("response.statusCode").should("eq", 204);

    cy.url().should("include", "/auth/register");
  });
});
