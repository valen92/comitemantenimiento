/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Certificacionprov;
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
public class CertificacionprovFacade extends AbstractFacade<Certificacionprov> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CertificacionprovFacade() {
        super(Certificacionprov.class);
    }
    
    public List<Certificacionprov> findCertificacion(int[] range, int idU) {
        String consulta = "SELECT n FROM Certificacionprov n where n.fkidEmpresas.idEmpresas = :idEmpresas";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idEmpresas", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
