# Repository Guidelines

## Project Structure & Module Organization
Backend entry point lives in `ruoyi-admin`; feature logic is split across `ruoyi-system`, `ruoyi-framework`, `ruoyi-common`, `ruoyi-user`, `ruoyi-quartz`, and `ruoyi-generator`. Shared configuration and security components stay in `ruoyi-framework`, while common constants belong in `ruoyi-common/src/main/java/com/ruoyi/common/constant`. The Vue 2 client is under `ruoyi-ui`, with views in `ruoyi-ui/src/views`, shared widgets in `ruoyi-ui/src/components`, and REST wrappers in `ruoyi-ui/src/api`. Database DDL and seed data are tracked in `sql/`, and docs and diagrams in `doc/`.

## Build, Test, and Development Commands
Run `mvn clean package -DskipTests` from the repo root for a full backend build, or `mvn spring-boot:run -pl ruoyi-admin -am` to launch the API with auto-reload. Inside `ruoyi-ui`, execute `npm install` once, `npm run dev` for local SPA development, and `npm run build:prod` to produce the production bundle in `ruoyi-ui/dist`. Use `./ry.sh start` (or `ry.bat start` on Windows) to boot both tiers with the default profile.

## Coding Style & Naming Conventions
Java code uses 4-space indentation, braces on new lines, and package names rooted at `com.ruoyi`. Service classes follow `XxxService`/`XxxServiceImpl`, and persistence mappers end with `Mapper`. Vue components use PascalCase directories and camelCase for methods, computed props, and data fields. Keep new constants or enums aligned with existing modules, and mirror backend controller names when adding entries to `ruoyi-ui/src/api`.

## Testing Guidelines
Server-side tests use JUnit 5 under `src/test/java`, mirroring production packages. Run `mvn test -pl ruoyi-admin -am` before submitting changes, and extend coverage when adding service logic or mappers. UI automation is manual; document key regression paths in your PR, and add Vue Test Utils specs when you introduce non-trivial client-side state.

## Commit & Pull Request Guidelines
Follow the existing short date tag plus imperative summary pattern (e.g., `10-14 tighten login throttle`) and reference related issues with `#id`. Pull requests should describe scope, list the validation commands you ran, call out SQL migrations touching `sql/`, and attach screenshots or GIFs for UI-facing updates. Note any configuration prerequisites or environment variable changes.

## Security & Configuration Tips
Do not check in production credentials. Datasource, Redis, and JWT secrets live in `ruoyi-admin/src/main/resources/application-*.yml`; edit local overrides instead. Frontend environment toggles reside in `.env.development`, `.env.staging`, `.env.production`, with sensitive keys stored in untracked `.env.local` files.
