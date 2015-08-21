/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Normas;
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
public class NormasFacade extends AbstractFacade<Normas> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;
    private controller.UsuariosFacade ejbFacade;
    private views.LoginController prueba;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NormasFacade() {
        super(Normas.class);
    }
    
    public List<Normas> findporLogin(int[] range, int idU) {
        String consulta = "select n from Normas n where n.fkidUsuarios.idUsuarios = :idUsuario";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
