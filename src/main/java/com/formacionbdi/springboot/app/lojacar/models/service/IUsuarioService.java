package com.formacionbdi.springboot.app.lojacar.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;

public interface IUsuarioService {
	public List<Usuarios> findAll();
	public Usuarios findById(Long id);
	public Usuarios save(Usuarios usuario);
	public void deleteById( Long id);
}
