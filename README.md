Welcome to Prode
================
Algo

Este es un proyecto utilizado para enseñar/aprender algunas herramientas en el marco de la asignatura "Análisis y Diseño de Algoritmos" del Departamento de Computación de la UNRC.

Getting Started
---------------

Aquí dejamos el resumén de lo utilizado hoy en el taller para generar el proyecto.

# Maven (http://maven.apache.org/)

* Maven es una herramienta para manejar el proyecto. Automatiza el build
(construye el software) describiendo sus dependencias y como es el proceso de
build.

* Esta descripción sigue las directivas de convención sobre configuración.

* Utiliza un archivo llamado pom.xml para la descripción de las dependencias
POM (Project Object Model)

* Basic commands
> `$ mvn --version`
>
>  `$ mvn package`
>  toma el código compilado y lo empaqueta en un formato distrubuible (como JAR)
>
>  `$ mvn compile`
>  compila el código fuente del proyecto
>
>  `$ mvn validate`
>  Valida que toda la información este disponible
>
>  `$ mvn test`
>  Corre la test suite
>
>  `$ mvn deploy`

* _Archetypes_ son templates para crear proyectos nuevos, hay millones y se
pueden hacer las propias

### Creando nuestro proyecto
```bash
    mvn archetype:generate -DgroupId=prode \
                           -DartifactId=prode-app \
                           -DarchetypeArtifactId=maven-archetype-quickstart \
                           -DinteractiveMode=false
```

La siguiente es la estructura de archivos y directorios generada

```
project
|-- pom.xml
`-- src
    |-- main
    |   `-- java
    |       `-- App.java
    `-- test
        `-- java
            `-- AppTest.java
```

Podemos utilizar varios comandos maven para correr los test, compilar y generar el JAR del proyecto

```
$ mvn test
$ mvn compile
$ mvn package
```

Para correr el proyecto se puede invocar desde consola directamente al JAR generado

```
  $ java -cp target/prode-app-1.0-SNAPSHOT.jar prode.App
```

Limpiar el proyecto antes de subir a GIT

```
mvn clean
```

## Instalando plugins

Para instalar algo en el proyecto hay que describirlo en el POM, por ejemplo surefire es un plugin utilizado para generar reportes en maven, para instalarlo hay que agregar el siguiente código al archivo `pom.xml`

```
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.18.1</version>
        <configuration>
            <reportFormat>brief</reportFormat>
            <trimStackTrace>true</trimStackTrace>
            <useFile>false</useFile>
            <includes>
                <include>**/*Spec*.java</include>
                <include>**/*Test*.java</include>
            </includes>
            <excludes>
                <exclude>**/helpers/*</exclude>
                <exclude>**/*$*</exclude>
            </excludes>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

# MySql
La base de datos relacional que utilizaremos es MySql

```mysql -u <user> -p```

# ActiveJDBC (http://javalite.io/activejdbc)
Es el intermediario entre nuestra aplicación y la base de datos. Es un ORM (Object Relational Mapping) rápido y ágil.

### Instalar las dependencias
- activejdbc
- mysql-connector-java

Actualizar el `pom.xml` con las siguientes dependencias

```
<dependency>
  <groupId>org.javalite</groupId>
  <artifactId>activejdbc</artifactId>
  <version>${activejdbc.version}</version>
</dependency>

<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>6.0.6</version>
</dependency>
```

### Instalar otros 2 plugins:
- activejdbc-instrumentation
- db-migrator-maven-plugin (http://javalite.io/database_migrations)

```
<build>
  <plugins>
    <plugin>
        <groupId>org.javalite</groupId>
        <artifactId>activejdbc-instrumentation</artifactId>
        <version>${activejdbc.version}</version>
        <executions>
            <execution>
                <phase>process-classes</phase>
                <goals>
                    <goal>instrument</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

    <plugin>
      <groupId>org.javalite</groupId>
      <artifactId>db-migrator-maven-plugin</artifactId>
      <version>${activejdbc.version}</version>
      <configuration>
          <configFile>${project.basedir}/src/main/resources/database.properties</configFile>
          <environments>${environments}</environments>
      </configuration>
      <executions>
          <execution>
              <id>dev_migrations</id>
              <phase>validate</phase>
              <goals>
                  <goal>migrate</goal>
              </goals>
          </execution>
      </executions>
      <dependencies>
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>6.0.6</version>
          </dependency>
      </dependencies>
    </plugin>
```

```
<properties>
    <activejdbc.version>2.0</activejdbc.version>
    <environments>development.test,development</environments>
    <project.basedir>...</project.basedir>
  </properties>
```

Crear el archivo de configuración de la base de datos `{basedir}/src/resources/database.properties`
```
development.driver=com.mysql.jdbc.Driver
development.username=<user>
development.password=<passwd>
development.url=jdbc:mysql://localhost/prode

development.test.driver=com.mysql.jdbc.Driver
development.test.username=<user>
development.test.password=<passwd>
development.test.url=jdbc:mysql://localhost/prode_test?nullNamePatternMatchesAll=true
```

Algunos comandos del plugin db-migrator

```
mvn db-migrator:create

mvn db-migrator:migrate

mvn db-migrator:new -Dname=create_users_table

mvn  db-migrator:help
```

### Flujo de trabajo con el plugin db-migrator
Crear las base de datos
```
  $ mvn db-migrator:create
```
Crear una migración nueva
```
  $ mvn db-migrator:new -Dname=create_people_table
```

Escribir el SQL para crear la tabla en el archivo generado por el comando anterior

```
CREATE TABLE users (
  id int(11) auto_increment PRIMARY KEY,
  username VARCHAR(128),
  password VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;
```

Correr la migración

```
$ mvn db-migrator:migrate
```

En este punto nuestra base de Datos está creada y tiene la tabla Users


### ActiveJDBC Models
Ahora para utilizar ActiveJDBC lo primero que podemos hacer es crear un modelo, para esto hay que crear una clase que extienda de `activejdbc.Model`

```
package prode;

import org.javalite.activejdbc.Model;

public class User extends Model {
}
```

Notar que al extender de `activejdbc.Model` heredamos todas las bondades de un modelo activeJDBC, esto significa que, nuestros objectos de tipo `User` saben guardarse y recuperarse de la DB sin necesidad de tener que escribir código SQL.

Para más información mirar la API de [activejdbc.Model](http://javalite.github.io/activejdbc/1.4.11/org/javalite/activejdbc/Model.html).

# TDD
Vamos a jugar un poco con la idea de crear una especificación primero (el qué) y después escribir su implementación (el como). Test Driven Development es una técnica que alienta escribir primero el test y después su código, de esta manera una vez resuelto el problema tenemos la prueba que funciona.

En nuestro proyecto no queremos tener usuarios sin `username`. Vamos a especificar esto con JUnit

# JUnit (http://junit.org/junit4/)
Es un framework muy sencillo para escribir pruebas (tests desde ahora).

Vamos a describir un modelo User en donde vamos a requerir la presencia de username para que el usuario sea valido en nuestro sistema

```
package prode;

import prode.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
   @Before
    public void before(){
        Base.open("default");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void validatePrecenseOfUsernames(){
        User user = new User();
        user.set("username", "");

        assertEquals(user.isValid(), false);
    }
}
```

Bastante declarativo. Si corremos los tests en este punto vamos a ver que fallan dado que no tenemos nada en el modelo User que nos invalide un usuario con esas características.

Entonces tenemos que trabajar para que los tests pasen ahora. Si actualizamos el modelo `User` con el siguiente código

```
package prode;

import org.javalite.activejdbc.Model;

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
  }
}
```

Los tests vuelven a pasar y voila, ya tenemos especificación, código y prueba del requerimiento.

limpiar tests con

```
import org.javalite.activejdbc.test.DBSpec;

public class UserTest extends DBSpec {
...
```

# Licence

This project is licensed under the MIT License - see the LICENSE.md file for details

Pivotal Tracker:
https://www.pivotaltracker.com/projects/2163709/settings
