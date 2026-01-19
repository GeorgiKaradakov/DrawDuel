describe("Dashboard", () => {
  const user = {
    email: "cypress@test.com",
    password: "Password123!",
  };

  beforeEach(() => {
    cy.login(user.email, user.password);
  });

  it("loads dashboard for authenticated user", () => {
    cy.visit("/dashboard");

    cy.contains("Dashboard");
  });
});
