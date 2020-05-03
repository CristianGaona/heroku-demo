# Ejemplo CRUD de Usuarios con Spring Boot, API RESTful, JPA, HIbernate, Postgresql, FreeMarker y VueJS
Tutorial para la construcción de una palicación web utilizando las tecnologías antes mencionadas, además se creara un pipeline para la integración y despliegue continuo.
## Herramientas necesarias
Su computadora debe tener instalado 
* JDK 8 u OpenJDK 8 en adelante, 
* PostgreSQL 9.5 en adelante en este caso se ha usuado la version 12.2 https://www.postgresql.org/download/
* Spring Tools 4 for Eclipse https://spring.io/tools

## Crear un proyecto utilizando Spring Tools for Eclipse
Nombre del proyecto heroku-demo o cualquier otro nombre, en donde la estructura del proyecto debe ser la siguiente:
Nota el nombre del paquete raíz no necesariamente puede contener el mismo nombre esto queda a conveniencia del desarrollador
```json
 src
│   └── main
│       ├── java
│       │   └── com
│       │       └── formacionbdi
│       │           └── springboot
│       │               └── app
│       │                   ├── lojacar
|       |                       └── controller
│       │                   │       ├── UsuarioController.java
│       │                   │       ├── ViewController.java
│       │                   │   ├── models
│       │                   │       └── dao
│       │                   │           ├── UsuarioDao.java
│       │                   │       └── entity
│       │                   │           ├── Usuarios.java
│       │                   │       └── service
│       │                   │           ├── IUsuarioService.java
│       │                   │           ├── UsuarioServImplement.java
│       │                   └── HerokuDemoApplication.java
│       └── resources
│           ├── static
│           │   ├── users.css
│           │   └── users.js
│           ├── templates
│           │   └── users.html
│           └── application.properties
│           └── import.sql
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```
## Dependencias del proyecto
Estas dependecias se encuentran en el [pom.xml](pom.xml)  del proyecto
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.2.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.formacionbdi.springboot.app.lojacar</groupId>
	<artifactId>heroku-demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>heroku-demo</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		   <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
        </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
		</dependency>
		
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-freemarker</artifactId>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```
## Creación de la Entidad
[Usuarios.java](src/main/java/com/formacionbdi/springboot/app/lojacar/models/entity/Usuarios.java) esta clase permite crear la respectiva tabla de usuarios con sus atributos en la base de datos seleccionada.
* @Entity es una anotación de JPA que nos permite manipular la base de datos a través de objetos, con esta anotación estamos mapeando la clase con una tabla de la base de datos
* @Table es una anotación de JPA  que nos permite renombrar el nombre de la tabla, es recomendable respetar mayúsculas y minúsculas.
* @GeneratedValue es una anotación de JPA que nos permite indicar la clave primaria misma que es auto incrementable
```java
package com.formacionbdi.springboot.app.lojacar.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuarios  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String apellido;
	private String correo;
	private int edad;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5108161301041062857L;
}

```
## Crear la interface [UsuarioDao](src/main/java/com/formacionbdi/springboot/app/lojacar/models/dao/UsuarioDao.java)
Esta interfaz extendiende de [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html) una interfaz de opreaciones CRUD generéricas en un repositorio para un tipo especifico, con esto se logra abstracción de código  para realizar un CRUD (create, read, update and delete)
```java
package com.formacionbdi.springboot.app.lojacar.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;

public interface UsuarioDao extends CrudRepository<Usuarios, Long> {

}

```
## Creación de la interface [IUsuarioService](https://github.com/CristianGaona/heroku-demo/blob/master/src/main/java/com/formacionbdi/springboot/app/lojacar/models/service/IUsuarioService.java)
A lo mejor en este momento esta interfarce no cumpla con la función adecuada por lo que es un ejemplo sencillo, pero cuando trabajamos con microservicios es muy útil porque permite el desacoplamiento de una clase, esto quiere decir que la interface es una funcionalidad abierta a la reutilización y extensibilidad. En este caso declaramos los métodos básicos de un CRUD para que sean implemnetados en la Clase UsuarioServImplement.java
```java
package com.formacionbdi.springboot.app.lojacar.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;

public interface IUsuarioService {
	public List<Usuarios> findAll();
	public Usuarios findById(Long id);
	public Usuarios save(Usuarios usuario);
	public void deleteById( Long id);
}
```
## Creación de la clase [UsuarioServImplement.java](https://github.com/CristianGaona/heroku-demo/blob/master/src/main/java/com/formacionbdi/springboot/app/lojacar/models/service/UsuarioServImplement.java)
En esta clase se puede observar las siguientes anotaciones:
* @Service contiene la lógica empresarial y los métodos de llamada  en la capa de repositorio.
* @Autowired permite la inyección de dependencias, en este caso se lo a utilizado para trabajar con una sola intancia y no crear una instancia nueva del objeto cada vez que se necesite la funcionalidad de determinadas clases.
* @Override permite sobrescribir un método
* @Transactional(readOnly = true) permite optimizar las operaciones de la capa de acceso a datos, aqui Hibernate solo busca entidades en mode lectura.
```java
package com.formacionbdi.springboot.app.lojacar.models.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.lojacar.models.dao.UsuarioDao;
import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;

@Service
public class UsuarioServImplement implements IUsuarioService {

	
    @Autowired
    private UsuarioDao usuarioDao;
    
	@Override
	@Transactional(readOnly = true)
	public List<Usuarios> findAll() {
		
		return (List<Usuarios>) usuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuarios findById(Long id) {
		
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	public Usuarios save(Usuarios usuario) {
		
		return usuarioDao.save(usuario);
	}

	@Override
	public void deleteById(Long id) {
		usuarioDao.deleteById(id);
	}

}

```
## Crear la clase [UsuarioController.java](https://github.com/CristianGaona/heroku-demo/blob/master/src/main/java/com/formacionbdi/springboot/app/lojacar/controllers/UsuarioController.java)
Esta clase nos permite manejar todos nuestros endpoints, ademas proporciona una implementación para agregar nuevos usuarios, buscar un lista de todos los usuarios, buscar un usuario especifíco y actualizar y eliminar un usuario.
```java
package com.formacionbdi.springboot.app.lojacar.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;
import com.formacionbdi.springboot.app.lojacar.models.service.IUsuarioService;
import com.formacionbdi.springboot.app.lojacar.models.service.UsuarioServImplement;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class UsuarioController {

	@Autowired
	private UsuarioServImplement usuuarioService;
	
	@GetMapping
	public List<Usuarios> Listar(){
		return usuuarioService.findAll();
	}
	
	@GetMapping("/{id}")
	public Usuarios detalle(@PathVariable Long id) {
		Usuarios usuario = usuuarioService.findById(id);
		return usuario;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuarios crear(@RequestBody Usuarios usuario) {
		return usuuarioService.save(usuario);
	}
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar (@PathVariable Long id) {
		usuuarioService.deleteById(id);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuarios update(@PathVariable Long id, @Valid @RequestBody Usuarios usuario) {
		    
		return usuuarioService.save(usuario);
	}
}

```
ESTO ES OPCIONAL

Ahora ya se puede levantar la aplicación, pero antes para probar 
deben agregar la dependecia H2 en el pon.xml, una base de datos en memoria que se crea cuando se levanta el proyecto y se destruye cuando se da de baja la aplicación por lo cual no existe persistencia de datos

pon.xml
```xml
<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
</dependency>
```
Deben crear el import.sql con los siguientes insert (solo es para pruebas)
import.sql
```sql
INSERT INTO `usuarios` (nombre, apellido, correo, edad ) VALUES('Cristian', 'Gaona', 'crgaonas24@gmail.com', 24);
INSERT INTO `usuarios` (nombre, apellido, correo, edad ) VALUES('Daniel', 'Cruz', 'dcruz34@hotmail.com', 25);
INSERT INTO `usuarios` (nombre, apellido, correo, edad) VALUES('Juan', 'Sandoval','jsando26@yahoo.es', 21 );
```
Deben configurar el archivo application.properties de la siguiente manera, en donde se indica el nombre de la aplicación, puerto en el que va a correer y la ultima linea sirve para ver como se generar los inserts por consola
```java
spring.application.name=servicio-usuarios
server.port=8003
logging.level.org.hibernate.SQL=debug
```
Los edpoints serian los siguientes:
* GET : localhost:8003/api/v1/users
imagen1
* GET : localhost:8003/api/v1/users/1 (puede ser 1, 2 o 3 esto es de acurdo al usuarios que quieran buscar)
imagen2
* POST: localhost:8003/api/v1/users
imagen3
* PUT: localhost:8003/api/v1/users/1
DELETE: localhost:8003/api/v1/users/1
* imagen4
Para probar todos estos endpoints se suguiere que lo realicen en Postman
