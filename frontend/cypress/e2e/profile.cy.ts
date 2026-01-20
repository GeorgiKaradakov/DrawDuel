describe("User Settings E2E", () => {
  let TEST_USER = {
    username: `cypressUser${Date.now()}`,
    email: `cypress@${Date.now()}test.com`,
    password: "Password123!",
  };

  before(() => {
    cy.register(TEST_USER.username, TEST_USER.email, TEST_USER.password);
    cy.visit("/dashboard");
  });

  beforeEach(() => {
    cy.login(TEST_USER.email, TEST_USER.password);
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

    cy.get('[data-cy="change-Username-input"]').clear().type(newUsername);

    cy.get('[data-cy="change-Username-submit"]').click();

    cy.contains("Username updated successfully!").should("be.visible"); //check for success toast
  });

  it("shows active devices", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.get('[data-cy="settings-sidebar-device-management"]').click();
    cy.get("table").within(() => {
      cy.contains("Operating System").should("be.visible");
      cy.contains("Browser").should("be.visible");
      cy.contains("Status").should("be.visible");
    });
  });

  it("does not allow revoking current session", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.get('[data-cy="settings-sidebar-device-management"]').click();

    cy.get('[data-cy="revoke-session-button-current-session"]').click();

    cy.contains("Cannot revoke current session").should("be.visible"); //check for error toast
  });

  it("changes password successfully", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();
    cy.get('[data-cy="settings-sidebar-security-settings"]').click();

    cy.get('[data-cy="current-password-input"]').type(TEST_USER.password);
    TEST_USER.password = "NewPassword123!";
    cy.get('[data-cy="new-password-input"]').type(TEST_USER.password);

    cy.get('[data-cy="change-password-button"]').click();

    cy.contains("Password updated successfully!").should("be.visible"); //check for success toast
  });

  it("updates email successfully", () => {
    const newEmail = `test_${Date.now()}@mail.com`;

    cy.get('[data-cy="navbar-AccountSettings"]').click();
    cy.scrollTo("right");

    cy.get('[data-cy="change-Email-input"]').clear().type(newEmail);

    cy.get('[data-cy="change-Email-submit"]').click();

    TEST_USER.email = newEmail;

    cy.contains("Email updated successfully!").should("be.visible"); //check for success toast
  });

  it("deletes account successfully", () => {
    cy.get('[data-cy="navbar-AccountSettings"]').click();

    cy.get('[data-cy="delete-profile-button"]').click();
    cy.url().should("include", "/auth/register");
  });
});
