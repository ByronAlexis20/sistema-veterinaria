package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class EnfermedadDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Enfermedad> getListaEnfermedades(String patron){
		List<Enfermedad> resultado = new ArrayList<Enfermedad>();
		Query query = getEntityManager().createNamedQuery("Enfermedad.busquedaPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Enfermedad>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Enfermedad> getListaEnfermedades(){
		List<Enfermedad> resultado = new ArrayList<Enfermedad>();
		Query query = getEntityManager().createNamedQuery("Enfermedad.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Enfermedad>) query.getResultList();
		return resultado;
	}
}
