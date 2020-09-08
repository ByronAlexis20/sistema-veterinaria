package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RazaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Raza> getListaRazas(String patron){
		List<Raza> resultado = new ArrayList<Raza>();
		Query query = getEntityManager().createNamedQuery("Raza.busquedaPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Raza>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Raza> getListaRazas(){
		List<Raza> resultado = new ArrayList<Raza>();
		Query query = getEntityManager().createNamedQuery("Raza.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Raza>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Raza> getListaRazasPorMascota(Integer idTipoMascota){
		List<Raza> resultado = new ArrayList<Raza>();
		Query query = getEntityManager().createNamedQuery("Raza.buscarPorTipo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idTipoMascota", idTipoMascota);
		resultado = (List<Raza>) query.getResultList();
		return resultado;
	}
}
