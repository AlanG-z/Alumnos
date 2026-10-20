# AGENTS.md

Jakarta EE 11 (webapp 6.0) student-management app: Java 17, Maven WAR, deployed on GlassFish, MySQL database `bdalumnos`. NetBeans project (`nbactions.xml`, `nb-configuration.xml` are committed — keep them in sync with `pom.xml`).

## Build & run

- `mvn package` builds the WAR into `target/Alumnos-1.0-SNAPSHOT.war`. No tests, lint, typecheck, or CI exist (`src/test` is empty) — verification is manual against a running GlassFish + MySQL.
- The `jakarta.jakartaee-api` dependency is `provided` scope, so the WAR bundles no libraries — it only runs on a server that supplies Jakarta EE 11. There is no embedded/dev-server mode.
- The app only runs where the GlassFish JDBC resource `java:app/jdbc/bdalumnos` exists; the pool/datasource is defined in `src/main/webapp/WEB-INF/glassfish-resources.xml` (MySQL on localhost:3306). Credentials are hardcoded there — do not copy them into other files.
- NetBeans Run/Debug/Profile all just run the Maven `package` goal and deploy (`nbactions.xml`); there is no separate deploy step or script.

## Architecture

Two parallel persistence paths over the same datasource — know which one your code uses:

- `sesiones/` — `@Stateless` EJB facades extending `AbstractFacade`, using the JTA persistence unit `AlumnosEJB_PU` (container-managed `EntityManager`).
- `GenericDAO` / `JPAUtil` — plain JDBC-style DAO using the RESOURCE_LOCAL persistence unit `AlumnosPU` (manual `EntityManager` + transaction handling; `JPAUtil` caches a single static `EntityManagerFactory`).

`persistence.xml` sets `exclude-unlisted-classes=true`: **every new entity class must be registered in both persistence units** or it will be invisible to one path. The NetBeans-generated `<class>` list also names the `@Embeddable` `ExamenPK`; keep it.

Two parallel UI stacks — putting a view in the wrong directory causes a 404:

- `WEB-INF/views/{alumno,carrera,materia}/{form,list}.jsp` — per-entity CRUD JSPs served by `controller/AlumnoServlet` (`/alumnos`), `CarreraServlet` (`/carreras`), `MateriaServlet` (`/materias`) via `GenericDAO`. Actions are selected with the `accion` request parameter (`nuevo`, `editar`, `eliminar`).
- `WEB-INF/vistas/*.jsp` — hand-rolled pages served by the single `controller/Manejador` servlet, which routes `/Agregar`, `/Listar`, `/Facultad`, `/MesasJulio`, `/SinExamenes`, `/DocentesMasDeDos` through a `switch` on the servlet path, using the EJB facades. Unknown paths return 404. New report-style pages go here.
- `controller/FacultadServlet` (`/FacultadServlet`) is an unused NetBeans template stub that prints placeholder HTML — there is **no** `/facultades` CRUD route. Facultad data is read through `Manejador` (`/Facultad`) and the Carrera form.

JAX-RS is mounted at `resources/` via `JakartaRestConfiguration`; the one endpoint is `GET /resources/jakartaee11` → `"ping Jakarta EE"`.

## Frontend

- All CSS lives in the shared `WEB-INF/views/header.jspf` (`<style>` block, CSS variables, light/dark themes via `data-theme` on `<html>`). All interactive JS (theme toggle, hamburger menu, student-table search/pagination, avatars, career badges) lives in `WEB-INF/views/footer.jspf`. Every page includes both — edit there, not in individual JSPs.
- New tables must be wrapped in `<div class="table-wrap">` for mobile overflow. New pages must set the active nav section via the `path`/`section` JSTL logic at the top of `header.jspf`.
- Nested `<ul class="dropdown">` menus need their own `list-style: none`. The browser UA sets `list-style-type: circle` on nested `ul`s, which beats inheritance from the top-level `nav > ul` rule — `list-style: none` on the outer menu alone will not remove submenu bullets.
- The Facultades dropdown in the nav iterates `${facultades}`, a **servlet-context** attribute populated once by `Manejador.init()` (`loadOnStartup=1`). `CarreraServlet` sets a same-named **request** attribute for `carrera/form.jsp`. If the dropdown shows "(sin facultades)", the context attribute was not set — a request attribute will not feed the nav.
- Dark mode persists in `localStorage` (`theme` key) and defaults to `prefers-color-scheme`.

## Conventions

- Keep `AGENTS.md` in sync: after any change to code, config, routes, or UI behavior, update this file if the change affects documented behavior.
- Code comments, UI strings, and user-facing error messages are in Spanish — match this when adding to existing pages.
- JSF is configured as Facelets (`nb-configuration.xml`) but the views are `.jsp` files — this is intentional; do not rename them to `.xhtml`.
- Entities use composite primary keys via separate `*PK` classes (e.g., `ExamenPK`) — follow the pattern for new entities. `Cargo` is a plain enum (`TITULAR`, `ADJUNTO`, `JTP`), not an entity.
