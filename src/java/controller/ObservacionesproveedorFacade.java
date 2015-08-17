/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Observacionesproveedor;
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
public class ObservacionesproveedorFacade extends AbstractFacade<Observacionesproveedor> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObservacionesproveedorFacade() {
        super(Observacionesproveedor.class);
    }
    
    public List<Observacionesproveedor> findporUsuario(int[] range, int idU, int idS) {
        String consulta = "select n from Observacionesproveedor n where n.fkidUsuarios.idUsuarios ";
        consulta = consulta + "= :idUsuario and n.fkidServiciosContrato.idServiciosContrato = :idServicio";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setParameter("idServicio", idS); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
