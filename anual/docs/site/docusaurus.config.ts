import { themes as prismThemes } from "prism-react-renderer";
import type { Config } from "@docusaurus/types";
import type * as Preset from "@docusaurus/preset-classic";

const config: Config = {
  title: "FridgeBridge docs",
  tagline: "Fighting hunter, feeding hope.",
  favicon: "img/favicon.ico",

  //TODO(marcos): change this once we have a domain
  url: "https://your-docusaurus-site.example.com",
  baseUrl: "/",

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: "FridgeBridge",
  projectName: "fridgebridge",

  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: "en",
    locales: ["en"],
  },

  presets: [
    [
      "classic",
      {
        docs: {
          routeBasePath: "/",
          sidebarPath: "./sidebars.ts",
          path: "../markdown",
        },
        theme: {
          customCss: "./src/css/custom.css",
        },
        blog: false,
      } satisfies Preset.Options,
    ],
  ],
  themes: [],

  themeConfig: {
    navbar: {
      title: "FridgeBridge",
      logo: {
        alt: "FridgeBridge Logo",
        src: "img/logo.png",
      },
      items: [
        {
          type: "docSidebar",
          sidebarId: "mainsidebar",
          position: "left",
          label: "Docs",
        },
        {
          href: "https://github.com/Lisandrogq/DDS-Grupo11/tree/main/anual",
          label: "GitHub",
          position: "right",
        },
      ],
    },
    footer: {
      style: "dark",
      links: [
        {
          title: "Links",
          items: [
            {
              label: "Site",
              href: "http://localhost:8000",
            },
            {
              label: "Signup",
              href: "http://localhost:8000/register/login",
            },
          ],
        },
        {
          title: "Social",
          items: [
            {
              label: "GitHub",
              href: "https://github.com/Lisandrogq/DDS-Grupo11/tree/main/anual",
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} FridgeBridge. Built with Docusaurus.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
