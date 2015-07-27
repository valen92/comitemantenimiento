/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Repuestosxempresas;
import entities.Repuestos;
import entities.Linearepuestos;
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
public class RepuestosxempresasFacade extends AbstractFacade<Repuestosxempresas> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RepuestosxempresasFacade() {
        super(Repuestosxempresas.class);
    }
    
    public List<Repuestosxempresas> findporTipoRepuesto(int[] range, int idTR) {
        String consulta = "SELECT n FROM Repuestosxempresas n LEFT JOIN Repuestos h where n.fkidRepuestos.idRepuestos = h.idRepuestos and h.fkidLineaRepuestos.idLineaRepuestos = :idLinea";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idLinea", idTR); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
