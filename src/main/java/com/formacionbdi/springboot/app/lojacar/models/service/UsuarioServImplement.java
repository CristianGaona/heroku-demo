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
