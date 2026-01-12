describe("Authentication flow", () => {
  const user = {
    username: "cypressUser",
    email: "cypress@test.com",
    password: "Password123!",
  };

  it("registers a user", () => {
    cy.visit("/auth/register");

    cy.get('input[name="username"]').type(user.username);
    cy.get('input[name="email"]').type(user.email);
    cy.get('input[name="pass"]').type(user.password);
    cy.get('input[name="passRepeat"]').type(user.password);
    cy.get('[type="checkbox"]').parent().click();

    cy.get('[data-cy="register-submit"]').click();

    cy.url().should("include", "/dashboard");
  });

  it("logs in", () => {
    cy.visit("/auth/login");

    cy.get('input[name="identifier"]').type(user.email);
    cy.get('input[name="pass"]').type(user.password);

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
