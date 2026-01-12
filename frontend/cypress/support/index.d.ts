declare namespace Cypress {
  interface Chainable {
    register(
      username: string,
      email: string,
      password: string,
    ): Chainable<void>;
    login(identifier: string, password: string): Chainable<void>;
    getUserIdFromToken(): Chainable<string>;
    logout(): Chainable<void>;
  }
}
