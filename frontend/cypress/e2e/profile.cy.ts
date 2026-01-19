describe("User Settings E2E", () => {
  let TEST_USER = {
    email: "cypress@test.com",
    password: "Password123!",
  };

  beforeEach(() => {
    cy.login(TEST_USER.email, TEST_USER.password);
    cy.visit("/dashboard");
  });

  it("loads the User Settings page", () => {
    cy.contains("Account Settings").click();

    cy.contains("Account").should("be.visible");
    cy.contains("Security").should("be.visible");
    cy.contains("Devices").should("be.visible");
  });

  it("updates username successfully", () => {
    const newUsername = `user_${Date.now()}`;

    cy.contains("Account Settings").click();

    cy.contains("Username").parent().find("input").clear().type(newUsername);

    cy.contains("Save Changes").click();

    cy.contains("Username updated successfully!").should("be.visible");
  });

  it("shows active devices", () => {
    cy.contains("Account Settings").click();

    cy.contains("Devices").click();
    cy.get("table").within(() => {
      cy.contains("Operating System").should("be.visible");
      cy.contains("Browser").should("be.visible");
      cy.contains("Status").should("be.visible");
    });
  });

  it("does not allow revoking current session", () => {
    cy.contains("Account Settings").click();

    cy.contains("Devices").click();

    cy.contains("current session")
      .parent()
      .parent()
      .within(() => {
        cy.contains("Revoke").click();
      });

    cy.contains("Cannot revoke current session").should("be.visible");
  });

  it("changes password successfully", () => {
    cy.contains("Account Settings").click();
    cy.contains("Security").click();

    cy.get('input[name="currentPass"]').type(TEST_USER.password);
    TEST_USER.password = "NewPassword123!";
    cy.get('input[name="newPass"]').type(TEST_USER.password);

    cy.contains("Change Password").click();

    cy.contains("Password updated successfully!").should("be.visible");
  });

  it("updates email successfully", () => {
    const newEmail = `test_${Date.now()}@mail.com`;

    cy.contains("Account Settings").click();
    cy.scrollTo("right");

    cy.contains("Email").parent().find("input").clear().type(newEmail);

    cy.contains("Email").parent().contains("Save Changes").click();

    cy.contains("Email updated successfully!").should("be.visible");
  });
});
