/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Actividades;
import entities.Servicioscontrato;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alexandra
 */
@Stateless
public class ServicioscontratoFacade extends AbstractFacade<Servicioscontrato> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServicioscontratoFacade() {
        super(Servicioscontrato.class);
    }
    
    public List<Servicioscontrato>obtenerTodosLosServicios(){
    EntityManager em = getEntityManager();
    Query q = em.createNamedQuery("Servicios.findAllDesc");
    return q.getResultList();
    }
    
    public List<Servicioscontrato> findporEmpresa(int[] range, int idEmpresa) {
        String consulta = "SELECT n FROM Servicioscontrato n where n.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idEmpresa); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Servicioscontrato> findporServicio(int[] range, int idEmpresa) {
        String consulta = "SELECT n FROM Serviciosxempproveedoras n where n.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idEmpresa); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Actividades> findporProActual(int[] range, int idU) {
        String consulta = "SELECT n FROM Actividades n LEFT JOIN Servicioscontrato s";
        consulta = consulta + ", Serviciosxempproveedoras u where n.fkidServiciosContrato";
        consulta = consulta + ".idServiciosContrato = s.idServiciosContrato and";
        consulta = consulta + " s.fkidServiciosxEmpProveedoras.idServiciosxEmpProveedoras";
        consulta = consulta + " = u.idServiciosxEmpProveedoras and s.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
