package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MascotaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Mascota> getListaMascotas(String patron){
		List<Mascota> resultado = new ArrayList<Mascota>();
		Query query = getEntityManager().createNamedQuery("Mascota.busquedaPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Mascota>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Mascota> getListaMascotasActivos(){
		List<Mascota> resultado = new ArrayList<Mascota>();
		Query query = getEntityManager().createNamedQuery("Mascota.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Mascota>) query.getResultList();
		return resultado;
	}
}
