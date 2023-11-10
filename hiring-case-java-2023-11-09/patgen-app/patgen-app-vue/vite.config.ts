import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  return {
    define: {
      PATGENBACKEND_EXTERNAL_URL: JSON.stringify(
        `${process.env.PATGENBACKEND_EXTERNAL_URL}`
      ),
      ZIPCODESERVICE_EXTERNAL_URL: JSON.stringify(
        `${process.env.ZIPCODESERVICE_EXTERNAL_URL}`
      ),
      CODESPACES: JSON.stringify(`${process.env.CODESPACES}`)
    },
    plugins: [
      [vue()],
      // Copy files for production
      {
        name: "client-host",
        transform(code, id) {
          if (
            (id.endsWith("dist/client/client.mjs") ||
              id.endsWith("dist/client/env.mjs")) &&
            typeof process.env.CODESPACE_NAME !== "undefined"
          ) {
            return code.replace(
              "__HMR_HOSTNAME__",
              JSON.stringify(
                `${process.env.CONTAINER_PATGENFRONTEND_DOMAIN}`
              )
            );
          }

          return code;
        },
      },
    ],
    server: {
      host: true,
      port: 8080,
      hmr: {
        protocol:
          typeof process.env.CODESPACE_NAME === "undefined"
            ? "ws"
            : "wss",
        clientPort:
          typeof process.env.CODESPACE_NAME === "undefined"
            ? 8080
            : 443,
      },
      watch: {
        usePolling: true,
      },
    },
    test: {
      globals: true,
      environment: "jsdom",
    }
  };
});
