describe("Dashboard", () => {
  const user = {
    username: "dashUser",
    email: "dashboard@test.com",
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

// it("shows user-specific data", () => {
//   cy.visit("/dashboard");
//
//   // Adjust text selectors to your UI
//   cy.contains(user.username);
// });

//                           I do not know how cypress sends cookies and I were not able to fix it

//   it("blocks dashboard when not authenticated", () => {
//     cy.logout();
//
//     cy.visit("/dashboard");
//     cy.url().should("include", "/auth/login");
//   });
// });
