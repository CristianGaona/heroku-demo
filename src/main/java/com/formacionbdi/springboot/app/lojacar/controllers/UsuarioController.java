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
