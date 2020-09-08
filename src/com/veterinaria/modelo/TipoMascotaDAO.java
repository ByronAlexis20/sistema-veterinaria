package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class TipoMascotaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<TipoMascota> getListaTipoMascota(String patron){
		List<TipoMascota> resultado = new ArrayList<TipoMascota>();
		Query query = getEntityManager().createNamedQuery("TipoMascota.busquedaPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<TipoMascota>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<TipoMascota> getListaTipoMascotas(){
		List<TipoMascota> resultado = new ArrayList<TipoMascota>();
		Query query = getEntityManager().createNamedQuery("TipoMascota.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoMascota>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoMascota> getListaMascotaPorTipo(Integer patron){
		List<TipoMascota> resultado = new ArrayList<TipoMascota>();
		Query query = getEntityManager().createNamedQuery("TipoMascota.porTipo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron );
		resultado = (List<TipoMascota>) query.getResultList();
		return resultado;
	}
}
