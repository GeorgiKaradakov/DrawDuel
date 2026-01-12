describe("Dashboard", () => {
  const user = {
    username: "dashUser",
    email: "dashboard@test.com",
    password: "Password123!",
  };

  before(() => {
    cy.register(user.username, user.email, user.password);
  });

  beforeEach(() => {
    cy.login(user.email, user.password);
  });

  it("loads dashboard for authenticated user", () => {
    cy.visit("/dashboard");

    cy.contains("Dashboard");
  });
});
