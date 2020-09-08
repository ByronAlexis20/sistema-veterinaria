package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;


public class UsuarioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuario(String usuario,String clave) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.buscarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usu", usuario);
		query.setParameter("cla", clave);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getValidarUsuario(String usuario,int idUsuario) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.validarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getListaUsuarios(String patron){
		List<Usuario> resultado = new ArrayList<Usuario>();
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getBuscarPorCedula(String cedula){
		List<Usuario> resultado = new ArrayList<Usuario>();
		Query query = getEntityManager().createNamedQuery("Usuario.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}
}
