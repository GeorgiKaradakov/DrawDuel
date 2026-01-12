describe("User profile management", () => {
  const timestamp = Date.now();

  const user = {
    username: `user_${timestamp}`,
    email: `user_${timestamp}@test.com`,
    password: "Test123!",
  };

  before(() => {
    cy.register(user.username, user.email, user.password);
  });

  it("loads update profile page with correct data", () => {
    cy.login(user.email, user.password);

    cy.getUserIdFromToken().then((userId) => {
      cy.visit(`/user-settings/update-profile/${userId}`);
    });

    cy.contains("Update Profile").should("exist");

    cy.get('input[name="username"]').should("have.value", user.username);
    cy.get('input[name="email"]').should("have.value", user.email);
  });

  it("updates profile successfully", () => {
    const updatedUser = {
      username: `updated_${timestamp}`,
      email: `updated_${timestamp}@test.com`,
    };

    cy.login(user.email, user.password);

    cy.getUserIdFromToken().then((userId) => {
      cy.visit(`/user-settings/update-profile/${userId}`);
    });

    cy.on("window:alert", (text) => {
      expect(text).to.eq("Update successful!");
    });

    cy.get('input[name="username"]').clear().type(updatedUser.username);
    cy.get('input[name="email"]').clear().type(updatedUser.email);

    cy.contains("Save changes").click();

    // Update local object for later assertions
    user.username = updatedUser.username;
    user.email = updatedUser.email;
  });

  it("deletes account and logs user out", () => {
    cy.login(user.email, user.password);

    cy.getUserIdFromToken().then((userId) => {
      cy.visit(`/user-settings/update-profile/${userId}`);
    });

    cy.contains("Delete account").click();

    cy.on("window:confirm", () => true);

    cy.url().should("include", "/auth/login");

    cy.window().then((win) => {
      expect(win.localStorage.getItem("accessToken")).to.be.empty;
    });
  });
});
