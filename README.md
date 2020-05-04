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
├── Procfile
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
Deben configurar el archivo **application.properties** de la siguiente manera, en donde se indica el nombre de la aplicación, puerto en el que va a correer y la ultima linea sirve para ver como se generar los inserts por consola
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
## Crear [ViewContgroller.java](https://github.com/CristianGaona/heroku-demo/blob/master/src/main/java/com/formacionbdi/springboot/app/lojacar/controllers/ViewController.java)
Esta clase nos permite conocetar todo nuestro backend con el Frontend, dentro del método ver() retornamos nuestro archivo User.html
```java
package com.formacionbdi.springboot.app.lojacar.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	
	@GetMapping("/")
	public String ver() {
		return "Users";
	}
}
 ```
## Crear una plantilla de vista de FreeMarker
Para esto debemos agregar la siguiente dependencia esto se lo puede hacer de la siguinete manera en el caso que utilicen Spring Tools (STS) 
* Clic derecho en el proyecto
* Seleccionamos casi en la prte final Spring -> Edit Startes
* Se presentara una ventana emergente con todos las dependecias disponibles
* Buscar Template Engines -> Apache Freemarker
* Presionar OK, y se agrega la dependencia.

En el caso que no utilicen STS se debe agregar la siguiente dependencia en el pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

Ahora si procesdemos a crear Users.html y agregar la librería de Vue Js junto con [Axios](https://vuejs.org/v2/cookbook/using-axios-to-consume-apis.html) que nos permite consumir y mostrar datos una API, en esta caso coumiremos la API que creamos con Spring Boot, axios es un cliente HTTP basado en promesas

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>Full stack CRUD Example with Spring Boot, PostgreSQL and VueJS</title>
  <link href="https://unpkg.com/bootstrap@3.4.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="/users.css" rel="stylesheet"/>
</head>
<body>
  <div class="container">
    <h1>User CRUD</h1>
    <main id="app">
      <router-view></router-view>
    </main>
  </div>

  <template id="user">
    <div>
      <h2>Personal Dates: </h2>
      <h5>Name: {{ user.nombre }}</h5>
      <h5>Last Name: {{ user.apellido }}</h5>
      <h5>Email: {{ user.correo }}</h5>
      <h5>Age: {{ user.edad }}</h5>
      
      <br/>
      <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
      <a>
        <router-link to="/">Back to user list</router-link>
      </a>
    </div>
  </template>

  <template id="user-list">
    <div>
      <div class="actions">
        <a class="btn btn-default">
          <router-link :to="{path: '/add-user'}">
            <span class="glyphicon glyphicon-plus"></span>
            Add User
          </router-link>
        </a>
      </div>
      <div class="filters row">
        <div class="form-group col-sm-3">
          <input placeholder="Search" v-model="searchKey" class="form-control" id="search-element" requred/>
        </div>
      </div>
      <table class="table">
        <thead>
        <tr>
          <th>Nombres</th>
          <th>Apellidos</th>
          <th>Correo</th>
          <th class="col-sm-2">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="usuarios in filteredProducts">
          <!-- tr v-for="product in products" -->
          <!-- tr v-for="product in products | filterBy searchKey in 'name'" -->
          <td>
            <a>
              <router-link :to="{name: 'user', params: {user_id: usuarios.id}}">{{ usuarios.nombre }}</router-link>
            </a>
          </td>
          <td>{{ usuarios.apellido }}</td>
         <td>{{ usuarios.correo }}</td>
          <td>
            <a class="btn btn-warning btn-xs">
              <router-link :to="{name: 'user-edit', params: {user_id: usuarios.id}}">Edit</router-link>
            </a>
            <a class="btn btn-danger btn-xs">
              <router-link :to="{name: 'user-delete', params: {user_id: usuarios.id}}">Delete</router-link>
            </a>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </template>

<template id="add-user">
    <div>
      <h2>Add new user</h2>
      <form @submit="createUser">
        <div class="form-group">
          <label for="add-nombre">Nombre</label>
          <input class="form-control" id="add-nombre" v-model="user.nombre" required/>
        </div>
        <div class="form-group">
          <label for="add-apellido">Apellido</label>
          <input class="form-control" id="add-apellido" v-model="user.apellido">
        </div>
        <div class="form-group">
          <label for="add-correo">Correo</label>
          <input class="form-control" id="add-correo" v-model="user.correo" required/>
        </div>
        <div class="form-group">
          <label for="add-edad">Edad</label>
          <input class="form-control" id="add-edad" v-model="user.edad" required/>
        </div>
        <button type="submit" class="btn btn-primary">Create</button>
        <a class="btn btn-default">
          <router-link to="/">Cancel</router-link>
        </a>
      </form>
    </div>
  </template>
  <template id="user-delete">
    <div>
      <h2>Delete user {{ user.nombre }}</h2>
      <form @submit="deleteUser">
        <p>The action cannot be undone.</p>
        <button type="submit" class="btn btn-danger">Delete</button>
        <a class="btn btn-default">
          <router-link to="/">Cancel</router-link>
        </a>
      </form>
    </div>
  </template>
<template id="user-edit">
    <div>
      <h2>Edit User</h2>
      <form @submit="updateUser">
        <div class="form-group">
          <label for="edit-nombre">Nombre</label>
          <input class="form-control" id="edit-nombre" v-model="user.nombre" required/>
        </div>
        <div class="form-group">
          <label for="edit-apellido">Apellido</label>
          <input  class="form-control" id="edit-apellido" v-model="user.apellido" required>
        </div>
        <div class="form-group">
          <label for="edit-correo">Correo</label>
          <input class="form-control" id="edit-correo" v-model="user.correo" required/>
        </div>
        <div class="form-group">
          <label for="edit-edad">Edad</label>
          <input class="form-control" id="edit-edad" v-model="user.edad" required/>
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
        <a class="btn btn-default">
          <router-link to="/">Cancel</router-link>
        </a>
      </form>
    </div>
  </template>
  <script src="https://unpkg.com/vue@2.5.22/dist/vue.js"></script>
  <script src="https://unpkg.com/vue-router@3.0.2/dist/vue-router.js"></script>
  <script src="https://unpkg.com/axios@0.18.0/dist/axios.min.js"></script>
  <script src="/users.js"></script>

</body>
</html>
```
## Crear [users.js](https://github.com/CristianGaona/heroku-demo/blob/master/src/main/resources/static/users.js)
aqui tenemos todas las funciones necesarias para realizar el CRUD desde la vista web y las correspondientes rutas.
```js
var usuarios = [];

function findUser(userId) {
    return usuarios[findProductKey(userId)];
}

function findProductKey(userId) {
    for (var key = 0; key < usuarios.length; key++) {
        if (usuarios[key].id == userId) {
            return key;
        }
    }
}

var usuarioServImplement = {
    findAll(fn) {
        axios
            .get('/api/v1/users')
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    findById(id, fn) {
        axios
            .get('/api/v1/users/' + id)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    create(user, fn) {
        axios
            .post('/api/v1/users', user)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    deleteUser(id, fn) {
        axios
            .delete('/api/v1/users/' + id)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },
    update(id, user, fn) {
        axios
          .put('/api/v1/users/' + id, user)
          .then(response => fn(response))
          .catch(error => console.log(error))
      }

}
var List = Vue.extend({
    template: '#user-list',
    data: function () {
        return { usuarios: [], searchKey: '' };
    },
    computed: {
        filteredProducts() {
            return this.usuarios.filter((user) => {
                return user.nombre.indexOf(this.searchKey) > -1
                    || user.apellido.indexOf(this.searchKey) > -1

            })
        }
    },
    mounted() {
        usuarioServImplement.findAll(r => { this.usuarios = r.data; usuarios = r.data })
    }
});

var User = Vue.extend({
    template: '#user',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    }
});

var UserEdit = Vue.extend({
    template: '#user-edit',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    },
    methods: {
        updateUser: function () {
        	usuarioServImplement.update(this.user.id, this.user, r => router.push('/'))
        }
    }
});

var UserDelete = Vue.extend({
    template: '#user-delete',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    },
    methods: {
    	deleteUser: function () {
            usuarioServImplement.deleteUser(this.user.id, r => router.push('/'))
        }
    }
});

var AddUser = Vue.extend({
    template: '#add-user',
    data() {
        return {
            user: { nombre: '', apellido: '', correo: '', edad: 0 }
        }
    },
    methods: {
        createUser() {
            usuarioServImplement.create(this.user, r => router.push('/'))
        }
    }
});

var router = new VueRouter({
    routes: [
        { path: '/', component: List },
        { path: '/user/:user_id', component: User, name: 'user' },
        { path: '/add-user', component: AddUser },
        { path: '/user/:user_id/edit', component: UserEdit, name: 'user-edit' },
        { path: '/user/:user_id/delete', component: UserDelete, name: 'user-delete' }
    ]
});

new Vue({
    router
}).$mount('#app')
```
### Crear user.css
```css
.actions {
  margin-bottom: 20px;
  margin-top: 20px;
}
```
## Probar la aplicación web
En est punto ya se puede probar la aplicación con una base de datos en memoria H2

Con la siguiente url = http://localhost:8003/

imagen5
## Crear base de datos en ProsgreSQL
Se asume que ya esta instaldo postgreSQL en la máquina local por tal razón se procede a realizar los siguinetes pasos:
* Crear una base de datos en PostgreSQL mediante pgadmin 4, no se debe crear la tabla usuarios ya que eso se crea atomaticamente al levantar la aplicación de Spring Boot. Por defecto PostgresSQL se ejecuta en el puerto 5432
* Agregar la dependecia, se puede revisar el [pom.xml](pom.xml) cargado en este repositorio
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
* configurar el archivo **application.properties** de la siguiente manera.
Como se puede observar en la configuración (spring.datasource.url) se coloca el puerto por defecto y el nombre de la base de datos, además usuario, contraseña, driver y el dialect en est caso lehe colocado PostgreSQL95Dialect porque funciona desde la version 9.5 en adelante.
```java
spring.application.name=servicio-usuarios
server.port=8003

spring.datasource.url=jdbc:postgresql://localhost:5432/db_heroku_demo?serverTimezone=America/Guayaquil
spring.datasource.username=postgres
spring.datasource.password=crisda24
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect
spring.jpa.hibernate.ddl-auto=create
logging.level.org.hibernate.SQL=debug

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```
* Para que se realicen los insert automáticos se debe quitar las comillas inversas (`usuarios`) que se coloco en el import.sql cuando ejecutamos con H2 y debe quedar de la siguiente manera:
```sql
INSERT INTO usuarios (nombre, apellido, correo, edad ) VALUES('Cristian', 'Gaona', 'crgaonas24@gmail.com', 24);
INSERT INTO usuarios (nombre, apellido, correo, edad ) VALUES('Daniel', 'Cruz', 'dcruz34@hotmail.com', 25);
INSERT INTO usuarios (nombre, apellido, correo, edad) VALUES('Juan', 'Sandoval','jsando26@yahoo.es', 21 );
```
En est punto ya se puede probar nuevamente la aplicación web con la siguiente url= [http://localhost:8003/](http://localhost:8003/) con la unica ventaja que ya no se utiliza un base de datos en memoría, ahora tenemos una base de datos local.
## Desplegando la aplicación en Heroku
Para desplegar nuestra aplicación en Heroku se debe realizar los siguientes pasos:
* Crear una cuenta en Heroku: https://www.heroku.com/
* Crear una app en heroku en el siguiente enlace se puede apreciar como crear una app en heroku: https://portalmybot.com/guia/heroku/cuenta-configuracion-app
* En **application.properties** volvemos a la configuración anterior con la base de datos en memoria H2
```java
spring.application.name=servicio-usuarios
server.port=8003
logging.level.org.hibernate.SQL=debug
```
* En el **import.sql** volvemos a agregar las comillas inversas a usuarios
```sql
INSERT INTO `usuarios` (nombre, apellido, correo, edad ) VALUES('Cristian', 'Gaona', 'crgaonas24@gmail.com', 24);
INSERT INTO `usuarios` (nombre, apellido, correo, edad ) VALUES('Daniel', 'Cruz', 'dcruz34@hotmail.com', 25);
INSERT INTO `usuarios` (nombre, apellido, correo, edad) VALUES('Juan', 'Sandoval','jsando26@yahoo.es', 21 );
```
Nota: Estos dos últimos pasos mencionadoss se los hace con la finalidad de que al momento de desplegar nuestra aplicación web en Heroku no tengamos errores con la base de datos local. Pero más adelante se hará uso de una base de datos en la nube.
* En la raíz del proyecto crear El archivo Procfile con la siguiente configuración
```java
web: java -Dserver.port=$PORT -jar target/heroku-demo-0.0.1-SNAPSHOT.jar
```
* En este punto se recomienda subir nuestra aplicación a GitHub un repositorio remoto se asume que se conoce este procedimiento, si no es el caso se recomienda ver el siguiente tutorial [subir proyecto a Git Hub](https://www.ecodeup.com/como-subir-el-codigo-de-tu-proyecto-a-github/). 
* Una vez que se subió el proyecto a Git Hub, se procede a conectarlo con Heroku e implementará automáticamente su app siempre que se actualice su repositorio GitHub creado. Como se observa en la imagen

IMAGEN 6

Una vez realizado todo eso se procede abrir la aplicacación en la siguiente url = http://nombre_app_en_heroku.heroku.com o en la parte superior derecha del Dashboard de Heroku donde dice open asi como se observa en la imagen

Imagen 7

Finalmente así se observa la aplicación desplegada en Heroku:

Imagen 8

## Crear base de datos PostgreSQL en la nube
* Dentro del Dashboard de Heroku -> Resources -> Add-ons y buscamos Heroku PostgreSQL como se observa en la imagen

Imagen 9

* Entramos a Heroku PostgreSQL y nos ubicamos en settings -> view Credentials, y se vizualizará lo siguiente:
```java
Host:  ec2-34-233-186-251.compute-1.amazonaws.com
Database: d5a7l04gn4h44e
User: hgcltlpdmzsulp
Port: 5432
Password: f50e5976a18e6434079897e5b955f0d5970a1bd88757b29345d6a2e65d1968b5
URI: postgres://hgcltlpdmzsulp:f50e5976a18e6434079897e5b955f0d5970a1bd88757b29345d6a2e65d1968b5@ec2-34-233-186-251.compute-1.amazonaws.com:5432/d5a7l04gn4h44e
```
Una vez que se obtenga todas las credenciales las reeplazamos en nuestra **application.properties**
```java
spring.application.name=servicio-usuarios
server.port=8003


spring.datasource.url=jdbc:postgresql://ec2-34-233-186-251.compute-1.amazonaws.com:5432/d5a7l04gn4h44e?serverTimezone=America/Guayaquil
spring.datasource.username=hgcltlpdmzsulp
spring.datasource.password=f50e5976a18e6434079897e5b955f0d5970a1bd88757b29345d6a2e65d1968b5
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect
spring.jpa.hibernate.ddl-auto=create

logging.level.org.hibernate.SQL=debug
spring.freemarker.suffix=.html

#spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```
En el **import.sql** volvemos a quitar las comillas inversas a usuarios
```sql
INSERT INTO usuarios (nombre, apellido, correo, edad ) VALUES('Cristian', 'Gaona', 'crgaonas24@gmail.com', 24);
INSERT INTO usuarios (nombre, apellido, correo, edad ) VALUES('Daniel', 'Cruz', 'dcruz34@hotmail.com', 25);
INSERT INTO usuarios (nombre, apellido, correo, edad) VALUES('Juan', 'Sandoval','jsando26@yahoo.es', 21 );
```
Finalmente para subimos nuestros cambios al repositorio de Git Hub y Heroku al detectar un cambio realiza el deploy automático de la aplicación, porque así lo configuramos anteriromente, y se puede visualizar nuevamente la aplicación web. En este caso sería [https://heroku-demo-cris.herokuapp.com](https://heroku-demo-cris.herokuapp.com/#/).

