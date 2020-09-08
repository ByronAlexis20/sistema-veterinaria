package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Perfil> getListaPerfiles(String patron){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getListaPerfilesActivos(){
		List<Perfil> resultado = new ArrayList<Perfil>();
		Query query = getEntityManager().createNamedQuery("Perfil.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
}
