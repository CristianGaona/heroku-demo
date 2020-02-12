package com.formacionbdi.springboot.app.lojacar.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.springboot.app.lojacar.models.entity.Usuarios;

public interface UsuarioDao extends CrudRepository<Usuarios, Long> {

}
