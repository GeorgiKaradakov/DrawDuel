import { defineConfig } from "cypress";

export default defineConfig({
  projectId: "whddh3",
  e2e: {
    baseUrl: "http://localhost:5173",
    env: {
      API_URL: "http://localhost:8080",
    },
    setupNodeEvents(on, config) {},
  },
});
