# GEMINI.md

## Project Overview

This project is a comprehensive **Property Management System** based on the **RuoYi-Vue** ecosystem. It integrates a Spring Boot backend, a Vue.js web administration portal, and a UniApp mobile application (targeting WeChat Mini Programs).

### Architecture

*   **Backend:** Spring Boot (Java 8+), centralized architecture.
*   **Admin Frontend:** Vue.js 2.x + Element UI.
*   **Mobile Frontend:** UniApp (Vue syntax) supporting WeChat Mini Programs, H5, and native apps.

## Directory Structure

*   `ruoyi-admin/`: Main backend application entry point (Spring Boot).
*   `ruoyi-framework/`: Core framework configuration (Security, WebConfig, etc.).
*   `ruoyi-system/`: System modules (User, Role, Menu, Dict, etc.).
*   `ruoyi-common/`: Common utility classes and constants.
*   `ruoyi-quartz/`: Scheduled task management.
*   `ruoyi-generator/`: Code generation module.
*   `ruoyi-ui/`: The Vue.js based admin dashboard source code.
*   `ruoyi-app/`: The UniApp source code for the mobile client.
*   `sql/`: Database initialization scripts (MySQL).
*   `bin/`: Scripts for cleaning and running the backend.

## Building and Running

### 1. Backend (Spring Boot)

*   **Prerequisites:** JDK 1.8+, Maven 3.0+, MySQL 5.7+.
*   **Setup:**
    1.  Create a MySQL database (e.g., `ry-vue`).
    2.  Execute SQL scripts in `sql/` to initialize tables and data.
    3.  Update database connection in `ruoyi-admin/src/main/resources/application-druid.yml`.
*   **Build:**
    ```bash
    mvn clean package
    ```
*   **Run:**
    ```bash
    # Using built JAR
    java -jar ruoyi-admin/target/ruoyi-admin.jar

    # Or using the helper script (Windows)
    ./ry.bat
    ```

### 2. Admin Frontend (Web)

*   **Path:** `ruoyi-ui/`
*   **Prerequisites:** Node.js >= 8.9, npm >= 3.0.0.
*   **Setup & Run:**
    ```bash
    cd ruoyi-ui
    npm install
    npm run dev
    ```
    Access at: `http://localhost:80` (proxies requests to backend).
*   **Build for Production:**
    ```bash
    npm run build:prod
    ```

### 3. Mobile App (UniApp)

*   **Path:** `ruoyi-app/`
*   **Framework:** UniApp (cross-platform).
*   **Tools:** HBuilderX (recommended) or standard CLI.
*   **Run:**
    *   Import the `ruoyi-app` directory into HBuilderX.
    *   Run -> Run to MiniProgram Simulator -> WeChat Developer Tools.
    *   Ensure `config.js` points to your running backend API.

## Development Conventions

*   **Backend:**
    *   Follow standard Layered Architecture: Controller -> Service -> Mapper -> Domain.
    *   Use `ruoyi-common` for shared utilities to avoid duplication.
    *   **Security:** Uses Spring Security + JWT. Ensure all new API endpoints are properly secured or explicitly whitelisted.
*   **Frontend (Web):**
    *   Components are in `src/components`.
    *   API calls are encapsulated in `src/api`.
    *   Views are in `src/views`.
    *   Follow Vue 2 Options API style.
*   **Mobile:**
    *   Follow UniApp conventions.
    *   Pages are defined in `pages.json`.
    *   Static assets in `static/`.

## Key Technologies

*   **Backend:** Spring Boot, MyBatis, Druid, PageHelper, Fastjson, JWT.
*   **Frontend:** Vue, Vuex, Element UI, Axios.
*   **Mobile:** UniApp, uView (or uni-ui), Vue.
