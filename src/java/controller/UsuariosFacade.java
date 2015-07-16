/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

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
        Usuarios usuario = new Usuarios ();
        String consulta = "select n from Usuarios n where n.fkidPerfil.idPerfil = :idPerfil";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idPerfil", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        System.out.println(""+idU);
        return q.getResultList();
    }
    
}
