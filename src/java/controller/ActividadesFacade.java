/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Actividades;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alexandra
 */
@Stateless
public class ActividadesFacade extends AbstractFacade<Actividades> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadesFacade() {
        super(Actividades.class);
    }
    
    public String findEmpresa(int idU) {
        String consulta = "select n.fkidEmpresa.idEmpresa from Usuarios n where n.idUsuario = :idUsuario";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        return q.getSingleResult().toString();
    }
    
}
