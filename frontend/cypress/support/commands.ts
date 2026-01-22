/// <reference types="cypress" />
// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
//
// declare global {
//   namespace Cypress {
//     interface Chainable {
//       login(email: string, password: string): Chainable<void>
//       drag(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       dismiss(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       visit(originalFn: CommandOriginalFn, url: string, options: Partial<VisitOptions>): Chainable<Element>
//     }
//   }
// }

Cypress.Commands.add("register", (username, email, password) => {
  const formData = new FormData();
  formData.append("username", username);
  formData.append("email", email);
  formData.append("pass", password);
  formData.append("repeatPass", password);
  formData.append("ipAddress", "127.0.0.1");
  formData.append("userAgent", "Cypress");
  formData.append("location", "Test");
  formData.append("profileImage", undefined);

  cy.request({
    method: "POST",
    url: `${Cypress.env("API_URL")}/api/auth/register`,
    body: formData,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
});

Cypress.Commands.add("login", (identifier, password) => {
  cy.request("POST", `${Cypress.env("API_URL")}/api/auth/login`, {
    identifier,
    password: password,
  }).then((res) => {
    window.localStorage.setItem("accessToken", res.body.accessToken);
  });
});

Cypress.Commands.add("getUserIdFromToken", () => {
  return cy.window().then((win) => {
    const token = win.localStorage.getItem("accessToken");
    expect(token).to.exist;

    const payload = JSON.parse(atob(token!.split(".")[1]));
    return payload.sub;
  });
});

Cypress.Commands.add("logout", () => {
  const accessToken = window.localStorage.getItem("accessToken");
  cy.request({
    method: "POST",
    url: `${Cypress.env("API_URL")}/api/auth/logout`,
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  window.localStorage.removeItem("accessToken");
});
