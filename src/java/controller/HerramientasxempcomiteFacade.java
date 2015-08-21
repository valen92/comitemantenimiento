/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Herramientasxempcomite;
import entities.Herramientas;
import entities.Tipoherramientas;
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
public class HerramientasxempcomiteFacade extends AbstractFacade<Herramientasxempcomite> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HerramientasxempcomiteFacade() {
        super(Herramientasxempcomite.class);
    }
    
    public List<Herramientasxempcomite> findporTipoHerramienta(int[] range, int idTH) {
        String consulta = "SELECT n FROM Herramientasxempcomite n LEFT JOIN Herramientas h where n.fkidHerramientas.idHerramientas = h.idHerramientas and h.fkidTipoHerramientas.idTipoHerramientas = :idTipo";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idTipo", idTH); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Herramientasxempcomite> findporTipoHerramientaM(int[] range, int idTH, int idU) {
        String consulta = "SELECT n FROM Herramientasxempcomite n LEFT JOIN Herramientas h, Usuarios u "
                + "where n.fkidHerramientas.idHerramientas = h.idHerramientas and "
                + "h.fkidTipoHerramientas.idTipoHerramientas = :idTipo and n.fkidEmpresas.idEmpresas = "
                + "u.fkidEmpresas.idEmpresas and u.idUsuarios = :idUsuario";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("idTipo", idTH); //Variable a pasar de la sesión
        q.setParameter("idUsuario", idU); //Variable a pasar de la sesión
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
