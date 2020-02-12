package com.formacionbdi.springboot.app.lojacar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;
import com.formacionbdi.springboot.app.lojacar.models.service.IUsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	private IUsuarioService usuuarioService;
	
	@GetMapping("/listar")
	public List<Usuarios> Listar(){
		return usuuarioService.findAll();
	}
	
	@GetMapping("/ver/{id}")
	public Usuarios detalle(@PathVariable Long id) {
		Usuarios usuario = usuuarioService.finById(id);
		return usuario;
	}
}
