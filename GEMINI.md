# GEMINI.md

## Project Overview

This project is a management system for property owners' committees, built on the RuoYi-Vue framework. It consists of a backend management system and a WeChat Mini Program for residents.

**Backend:**

*   **Framework:** Spring Boot
*   **Database:** MySQL
*   **Build Tool:** Maven
*   **Key Modules:**
    *   `ruoyi-admin`: Main application entry point.
    *   `ruoyi-framework`: Core framework components.
    *   `ruoyi-system`: System management functionalities.
    *   `ruoyi-quartz`: Scheduled tasks.
    *   `ruoyi-generator`: Code generation.
    *   `ruoyi-common`: Common utility classes.

**Frontend (Web):**

*   **Framework:** Vue.js
*   **UI Library:** Element UI
*   **Build Tool:** npm/yarn

**Frontend (Mobile):**

*   **Platform:** WeChat Mini Program

## Building and Running

### Backend

1.  **Prerequisites:**
    *   JDK >= 1.8
    *   Maven >= 3.0
    *   MySQL >= 5.7

2.  **Database Setup:**
    *   Create a MySQL database named `ry-vue`.
    *   Import the SQL scripts `ry_20250522.sql` and `quartz.sql` from the `sql` directory into the database.

3.  **Configuration:**
    *   Modify the database connection settings in `ruoyi-admin/src/main/resources/application-druid.yml`.

4.  **Build:**
    *   In the `RuoYi-Vue-master` directory, run:
        ```bash
        mvn clean package
        ```

5.  **Run:**
    *   On Windows, you can use the provided batch script:
        ```bash
        ry.bat
        ```
        Then select option `1` to start the application.
    *   Alternatively, you can run the JAR file directly:
        ```bash
        java -jar ruoyi-admin/target/ruoyi-admin.jar
        ```

### Frontend (Web)

1.  **Prerequisites:**
    *   Node.js >= 8.9
    *   npm >= 3.0.0

2.  **Installation:**
    *   Navigate to the `ruoyi-ui` directory:
        ```bash
        cd ruoyi-ui
        ```
    *   Install dependencies:
        ```bash
        npm install
        ```

3.  **Development:**
    *   Run the development server:
        ```bash
        npm run dev
        ```
    *   Access the application at `http://localhost:80`.

4.  **Production Build:**
    *   Build the project for production:
        ```bash
        npm run build:prod
        ```

### Frontend (WeChat Mini Program)

*   The source code for the WeChat Mini Program is located in the `ruoyi-app` directory.
*   Use WeChat DevTools to import and run the project.

## Development Conventions

*   The project follows the standard RuoYi-Vue development conventions.
*   Refer to the official RuoYi documentation for more details: [http://doc.ruoyi.vip](http://doc.ruoyi.vip)
