package com.veterinaria.modelo;

import java.util.List;

import javax.persistence.Query;

public class PermisoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Permiso> getPermisos(int idPerfil) {
		List<Permiso> resultado; 
		Query query = getEntityManager().createNamedQuery("Permiso.buscarPorPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfil", idPerfil);
		resultado = (List<Permiso>) query.getResultList();
		return resultado;
	}
}
