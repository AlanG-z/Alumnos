# Sistema de Alumnos

Aplicación web para la gestión de alumnos, carreras, materias y docentes de una
facultad. Desarrollada como ejercicio académico con **Jakarta EE 11**, desplegada
en **GlassFish** y persistiendo en **MySQL**.

Repo: <https://github.com/AlanG-z/Alumnos>

## Características

- CRUD completo de **alumnos**, **carreras**, **materias** y **docentes**.
- Los docentes se asocian a una o varias materias (relación many-to-many).
- Reportes: mesas de examen de julio, alumnos sin exámenes en el año y docentes
  que dictan más de dos materias.
- Menú de navegación con secciones agrupadas, buscador con paginación,
  modo claro/oscuro y diseño responsive (menú hamburguesa en pantallas chicas).

## Requisitos

| Componente | Versión |
| --- | --- |
| JDK | 17 o superior |
| Maven | 3.6+ (o usar el integrado en NetBeans) |
| Servidor | Eclipse GlassFish 8 (Jakarta EE 11) |
| Base de datos | MySQL 8 en `localhost:3306` |
| Driver | MySQL Connector/J (JDBC 4.2+) |

## Base de datos

Los nombres de tabla y columna están fijados por las anotaciones JPA
(`@Table(name = "...", catalog = "bdalumnos")`), por lo que la base **debe** llamarse
`bdalumnos`.

```sql
CREATE DATABASE bdalumnos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bdalumnos;

CREATE TABLE facultad (
  idfacultad INT NOT NULL AUTO_INCREMENT,
  nombre     VARCHAR(45) NOT NULL,
  PRIMARY KEY (idfacultad)
) ENGINE=InnoDB;

CREATE TABLE carrera (
  idcarrera          INT NOT NULL AUTO_INCREMENT,
  nombre             VARCHAR(45) NOT NULL,
  facultad_idfacultad INT NOT NULL,
  PRIMARY KEY (idcarrera),
  CONSTRAINT fk_carrera_facultad
    FOREIGN KEY (facultad_idfacultad) REFERENCES facultad (idfacultad)
) ENGINE=InnoDB;

CREATE TABLE materia (
  idmateria INT NOT NULL AUTO_INCREMENT,
  nombre    VARCHAR(45) NOT NULL,
  PRIMARY KEY (idmateria)
) ENGINE=InnoDB;

CREATE TABLE alumno (
  idalumno         INT NOT NULL AUTO_INCREMENT,
  nombre           VARCHAR(45) NOT NULL,
  registro         VARCHAR(6)  NOT NULL,
  carrera_idcarrera INT NOT NULL,
  PRIMARY KEY (idalumno),
  CONSTRAINT fk_alumno_carrera
    FOREIGN KEY (carrera_idcarrera) REFERENCES carrera (idcarrera)
) ENGINE=InnoDB;

CREATE TABLE docente (
  iddocente INT NOT NULL AUTO_INCREMENT,
  nombre    VARCHAR(45) NOT NULL,
  cargo     VARCHAR(10) NOT NULL,   -- TITULAR | ADJUNTO | JTP
  PRIMARY KEY (iddocente)
) ENGINE=InnoDB;

-- Relación N:N docente <-> materia
CREATE TABLE docente_has_materia (
  docente_iddocente INT NOT NULL,
  materia_idmateria INT NOT NULL,
  PRIMARY KEY (docente_iddocente, materia_idmateria),
  CONSTRAINT fk_dhm_docente FOREIGN KEY (docente_iddocente)
    REFERENCES docente (iddocente),
  CONSTRAINT fk_dhm_materia FOREIGN KEY (materia_idmateria)
    REFERENCES materia (idmateria)
) ENGINE=InnoDB;

-- Clave compuesta: materia + alumno + fecha
CREATE TABLE examen (
  materia_idmateria INT NOT NULL,
  alumno_idalumno   INT NOT NULL,
  fecha             DATETIME NOT NULL,
  nota              INT NULL,
  PRIMARY KEY (materia_idmateria, alumno_idalumno, fecha),
  CONSTRAINT fk_examen_materia FOREIGN KEY (materia_idmateria)
    REFERENCES materia (idmateria),
  CONSTRAINT fk_examen_alumno FOREIGN KEY (alumno_idalumno)
    REFERENCES alumno (idalumno)
) ENGINE=InnoDB;
```

Datos mínimos para que el menú no aparezca vacío:

```sql
INSERT INTO facultad (nombre) VALUES ('Ingeniería'), ('Derecho');
INSERT INTO carrera (nombre, facultad_idfacultad)
  VALUES ('Sistemas', 1), ('Industrial', 1), ('Abogacía', 2);
INSERT INTO materia (nombre) VALUES ('Base de Datos'), ('Algoritmos'), ('Derecho Penal');
```

## Configuración

### 1. Credenciales de MySQL

Editá `src/main/webapp/WEB-INF/glassfish-resources.xml` y reemplazá los
placeholders con tus datos reales:

```xml
<property name="User"    value="TU_USUARIO_MYSQL"/>
<property name="Password" value="TU_PASSWORD_MYSQL"/>
```

> ⚠️ **No commitees credenciales reales.** El archivo usa placeholders a propósito.

El pool define el JNDI `java:app/jdbc/bdalumnos`, que es el que consumen las dos
unidades de persistencia.

### 2. Driver JDBC

Descargá [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) y
copiá el JAR a la librería del dominio:

```
glassfish/domains/domain1/lib/mysql-connector-j-<versión>.jar
```

Reiniciá el dominio después de copiarlo.

## Compilar y ejecutar

### Desde NetBeans

Abrí el proyecto y presioná **Run** (F6). NetBeans ejecuta `mvn package` y
despliega el WAR automáticamente.

### Desde la línea de comandos

```bash
mvn clean package
```

Genera `target/Alumnos-1.0-SNAPSHOT.war`, que se puede desplegar con:

```bash
asadmin deploy target/Alumnos-1.0-SNAPSHOT.war
```

La aplicación queda en <http://localhost:8080/alumnos/>.

> **Nota:** no hay pruebas automatizadas ni CI; la verificación es manual contra
> GlassFish + MySQL corriendo.

## Rutas disponibles

| Ruta | Método | Descripción |
| --- | --- | --- |
| `/alumnos` | GET | Lista de alumnos (buscador y paginación) |
| `/alumnos?accion=nuevo` | GET | Formulario de alta |
| `/alumnos?accion=editar&id=` | GET | Formulario de edición |
| `/alumnos` | POST | Guardar (`accion=guardar`) o eliminar (`accion=eliminar`) |
| `/carreras` | GET/POST | CRUD de carreras (mismo patrón que `/alumnos`) |
| `/materias` | GET/POST | CRUD de materias |
| `/docentes` | GET/POST | CRUD de docentes (incluye materias que dicta) |
| `/Facultad?codigoFacu=` | GET | Carreras de una facultad |
| `/Listar` | GET | Listado de alumnos (vista con EJB) |
| `/Agregar` | GET/POST | Alta de alumno (vista con EJB) |
| `/MesasJulio` | GET | Reporte: mesas de examen de julio |
| `/SinExamenes` | GET | Reporte: alumnos sin exámenes en el año |
| `/DocentesMasDeDos` | GET | Reporte: docentes con más de dos materias |
| `/resources/jakartaee11` | GET | Endpoint de prueba JAX-RS → `"ping Jakarta EE"` |

## Estructura del proyecto

```
src/main/java/com/mycompany/alumnos/
├── Alumno.java, Carrera.java, Materia.java, Docente.java,
│   Facultad.java, Examen.java, ExamenPK.java   # entidades JPA
├── Cargo.java                                  # enum: TITULAR, ADJUNTO, JTP
├── sesiones/                                   # fachadas EJB (@Stateless)
│   ├── AbstractFacade.java                     #   CRUD genérico
│   └── *Facade.java                            #   consultas por entidad
└── controller/
    └── Manejador.java                          # único servlet: CRUD + reportes

src/main/webapp/
├── index.jsp
└── WEB-INF/
    ├── views/                                  # vistas CRUD (JSP)
    │   ├── header.jspf / footer.jspf           #   CSS y JS compartidos
    │   └── alumno|carrera|materia|docente/
    ├── vistas/                                 # vistas de reportes
    ├── glassfish-resources.xml                 # pool JDBC (JNDI)
    ├── glassfish-web.xml
    └── web.xml
```

## Arquitectura

### Persistencia

Existe **una sola vía de acceso a datos**: las fachadas EJB de `sesiones/`, que
usan la unidad de persistencia JTA `AlumnosEJB_PU` con `EntityManager` inyectado.

- Las operaciones dentro de un método de fachada corren en **una sola
  transacción** (tipo `REQUIRED`), por lo que crear/actualizar un registro con sus
  relaciones se hace atómico.
- `persistence.xml` declara `exclude-unlisted-classes=true`: **toda entidad nueva
  debe listarse en la unidad de persistencia** o no será visible.

### Vistas

Todas las páginas incluyen `header.jspf` y `footer.jspf`:

- **CSS** (variables, tema claro/oscuro, componentes) → bloque `<style>` de `header.jspf`.
- **JS** (toggle de tema, menú hamburguesa, buscador/paginación de tablas,
  avatares y badges) → `footer.jspf`.

Para que una tabla nueva tenga buscador y paginación, alcanza con envolverla en
`<div class="table-wrap">` y marcarla con el atributo `data-datatable`.

## Tecnologías

- Jakarta EE 11 (Servlet 6, JSP/JSTL, JPA 3.2, EJB, JAX-RS)
- Java 17
- Maven (empaquetado WAR)
- MySQL 8 + Eclipse GlassFish 8
- JSP con JSTL, CSS y JavaScript vanilla (sin frameworks front-end)
