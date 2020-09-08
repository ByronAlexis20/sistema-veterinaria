package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ClienteMascotaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<ClienteMascota> getListaMascotaPorCliente(String patron, int idCliente){
		List<ClienteMascota> resultado = new ArrayList<ClienteMascota>();
		Query query = getEntityManager().createNamedQuery("ClienteMascota.buscarPorCliente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idCliente", idCliente);
		resultado = (List<ClienteMascota>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<ClienteMascota> getListaMascotaActivos(){
		List<ClienteMascota> resultado = new ArrayList<ClienteMascota>();
		Query query = getEntityManager().createNamedQuery("ClienteMascota.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<ClienteMascota>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<ClienteMascota> getListaDuenioMascota(int idMascota){
		List<ClienteMascota> resultado = new ArrayList<ClienteMascota>();
		Query query = getEntityManager().createNamedQuery("ClienteMascota.buscarPorDuenio");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMascota",idMascota);
		resultado = (List<ClienteMascota>) query.getResultList();
		return resultado;
	}
	
}
