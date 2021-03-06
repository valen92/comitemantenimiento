/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Actividades;
import entities.Empresas;
import entities.Herramientasxempcomite;
import entities.Repuestosxempresas;
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
public class UsuariosFacade extends AbstractFacade<Usuarios> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }
    public List findByIdUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     public List findLogin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List findByLoginUsuarios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Usuarios findporLogin(String nombreUsuarios) {
//        EntityManager em2=getEntityManager();
        Query q = em.createNamedQuery("Usuarios.findByUsuarioUsuario")
                .setParameter("usuarioUsuario", nombreUsuarios);
        List<Usuarios> list=q.getResultList();
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }
    
    public Usuarios findId(int idUsuarios) {
//        EntityManager em2=getEntityManager();
        Query q = em.createNamedQuery("Usuarios.findByIdUsuarios")
                .setParameter("usuarioUsuario", idUsuarios);
        List<Usuarios> list=q.getResultList();
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }
    
    public List<Usuarios> findporPerfil(int[] range, int idU) {
        String consulta = "select n from Usuarios n where n.fkidPerfil.idPerfil = :idPerfil";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idPerfil", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Usuarios> findUsuarioPerfil(int[] range, int idU) {
        String consulta = "select n from Usuarios n where n.idUsuarios = :idUsuario";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        System.out.println(""+idU);
        return q.getResultList();
    }
    
    public List<Usuarios> findporDelegado(int[] range, int idU) {
        String consulta = "select n from Usuarios n where n.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Herramientasxempcomite> findporHerramienta(int[] range, int idU) {
        String consulta = "select n from Herramientasxempcomite n where n.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Repuestosxempresas> findporRepuesto(int[] range, int idU) {
        String consulta = "select n from Repuestosxempresas n where n.fkidEmpresas.idEmpresas = :idEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresa", idU); //Variable a pasar de la sesión
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
    

    public List<Empresas> findAllMiembro() {
        String consulta = "select n from Empresas n where n.tipoEmpresa = :idPerfil";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idPerfil", "Comite"); //Variable a pasar de la sesión
        return q.getResultList();
    }
    

    public List<Empresas> findAllProveedor() {
        String consulta = "select n from Empresas n where n.tipoEmpresa = :idPerfil";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idPerfil", "Proveedor"); //Variable a pasar de la sesión
        return q.getResultList();
    }
    
}
