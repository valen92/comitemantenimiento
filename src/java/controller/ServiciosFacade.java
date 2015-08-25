/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Actividades;
import entities.Servicios;
import entities.Usuarios;
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
public class ServiciosFacade extends AbstractFacade<Servicios> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServiciosFacade() {
        super(Servicios.class);
    }
    
    public List<Actividades> findActividadServicio(int[] range, int idU, int idServicio) {
        String consulta = "SELECT n FROM Actividades n LEFT JOIN Servicioscontrato s, Usuarios u";
        consulta = consulta + " where n.fkidServiciosContrato.idServiciosContrato =";
        consulta = consulta + "s.idServiciosContrato and";
        consulta = consulta + " s.fkidEmpresas.idEmpresas = u.fkidEmpresas.idEmpresas";
        consulta = consulta + " and u.idUsuarios = :idUsuario and s.fkidServiciosxEmpProveedoras.";
        consulta = consulta +"fkidServicios.idServicios= :idServicio";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setParameter("idServicio", idServicio); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Actividades> findAsociadas(int[] range, int idU, int idServicio) {
        String consulta = "SELECT n FROM Actividades n LEFT JOIN Servicioscontrato s, Usuarios u";
        consulta = consulta + " where n.fkidServiciosContrato.idServiciosContrato =";
        consulta = consulta + "s.idServiciosContrato and";
        consulta = consulta + " s.fkidEmpresas.idEmpresas = u.fkidEmpresas.idEmpresas";
        consulta = consulta + " and u.idUsuarios = :idUsuario and s.fkidServiciosxEmpProveedoras.";
        consulta = consulta +"fkidServicios.idServicios= :idServicio and n.estadoActividad = 'Activo'";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setParameter("idServicio", idServicio); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Actividades> findFrecuencia(int[] range, int idU, int idServicio) {
        String consulta = "SELECT n FROM Actividades n LEFT JOIN Servicioscontrato s, Usuarios u";
        consulta = consulta + " where n.fkidServiciosContrato.idServiciosContrato =";
        consulta = consulta + "s.idServiciosContrato and";
        consulta = consulta + " s.fkidEmpresas.idEmpresas = u.fkidEmpresas.idEmpresas";
        consulta = consulta + " and u.idUsuarios = :idUsuario and s.fkidServiciosxEmpProveedoras.";
        consulta = consulta +"fkidServicios.idServicios= :idServicio and n.estadoActividad <> 'Activo'";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setParameter("idServicio", idServicio); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
