package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class VeterinarioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Veterinario> getListaVeterinarios(String patron){
		List<Veterinario> resultado = new ArrayList<Veterinario>();
		Query query = getEntityManager().createNamedQuery("Veterinario.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Veterinario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Veterinario> getBuscarPorCedula(String cedula){
		List<Veterinario> resultado = new ArrayList<Veterinario>();
		Query query = getEntityManager().createNamedQuery("Veterinario.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Veterinario>) query.getResultList();
		return resultado;
	}
}
