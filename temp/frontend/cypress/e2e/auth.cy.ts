describe("Authentication flow", () => {
  //different user details every time
  const user = {
    username: `cypressUser${Date.now()}`,
    email: `cypress_${Date.now()}@test.com`,
    password: "Password123!",
  };

  it("registers and logs in a user", () => {
    cy.visit("/auth/register");

    cy.get('[data-cy="register-username-input"]').type(user.username);
    cy.get('[data-cy="register-email-input"]').type(user.email);
    cy.get('[data-cy="register-password-input"]').type(user.password);
    cy.get('[data-cy="register-confirm-password-input"]').type(user.password);
    cy.get('[data-cy="register-terms-checkbox"]').click();

    cy.get('[data-cy="register-submit"]').click();
    cy.url().should("include", "/dashboard");

    // logout
    cy.get('[data-cy="logout-submit"]').click();
    cy.url().should("include", "/auth/login");

    // login again
    cy.get('[data-cy="login-identifier-input"]').type(user.email);
    cy.get('[data-cy="login-password-input"]').type(user.password);
    cy.get('[data-cy="login-submit"]').click();

    cy.url().should("include", "/dashboard");
  });
});
