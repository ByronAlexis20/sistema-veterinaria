package com.veterinaria.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ConsultaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Consulta> getListaConsultas(String patron){
		List<Consulta> resultado = new ArrayList<Consulta>();
		Query query = getEntityManager().createNamedQuery("Consulta.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Consulta>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Consulta> getConsulta(Integer patron){
		List<Consulta> resultado = new ArrayList<Consulta>();
		Query query = getEntityManager().createNamedQuery("Consulta.numFactura");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "" + patron + "");
		resultado = (List<Consulta>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getConsultaMax(){
		List<Integer> numeroFactura;
		Query query = getEntityManager().createNamedQuery("Consulta.numSiguienteFactura");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		numeroFactura = (List<Integer>) query.getResultList();
		return numeroFactura;
	}
	
}
