describe("Authentication flow", () => {
  //we add the date.now function because the tests use the real database and we cannot rerun them if they have the same credentials
  const user = {
    username: `cypressUser${Date.now()}`,
    email: `cypress@${Date.now()}test.com`,
    password: "Password123!",
  };

  it("registers a user", () => {
    cy.visit("/auth/register");

    cy.get('[data-cy="register-username-input"]').type(user.username);
    cy.get('[data-cy="register-email-input"]').type(user.email);
    cy.get('[data-cy="register-password-input"]').type(user.password);
    cy.get('[data-cy="register-confirm-password-input"]').type(user.password);
    cy.get('[data-cy="register-terms-checkbox"]').click();

    cy.get('[data-cy="register-submit"]').click();

    cy.url().should("include", "/dashboard");
  });

  it("logs in", () => {
    cy.visit("/auth/login");

    cy.get('[data-cy="login-identifier-input"]').type(user.email);
    cy.get('[data-cy="login-password-input"]').type(user.password);

    cy.get('[data-cy="login-submit"]').click();

    cy.url().should("include", "/dashboard");
  });

  it("logs out", () => {
    cy.login(user.email, user.password);

    cy.visit("/dashboard");
    cy.get('[data-cy="logout-submit"]').click();

    cy.url().should("include", "/auth/login");
  });
});
