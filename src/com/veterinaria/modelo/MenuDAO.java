package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MenuDAO extends ClaseDAO{
	public Menu getMenuPadre(Integer idMenuPadre) {
		try {
			Query query = getEntityManager().createNamedQuery("Menu.buscarPadre");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("idMenuPadre", idMenuPadre);
			return (Menu) query.getSingleResult();
		}catch(Exception ex) {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getListaMenuAcivos(){
		List<Menu> resultado = new ArrayList<Menu>();
		Query query = getEntityManager().createNamedQuery("Menu.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Menu>) query.getResultList();
		return resultado;
	}
}
