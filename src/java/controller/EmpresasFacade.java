/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entities.Empresas;
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
public class EmpresasFacade extends AbstractFacade<Empresas> {
    @PersistenceContext(unitName = "comitemantenimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresasFacade() {
        super(Empresas.class);
    }

    public List<Empresas> findAllProveedor() {
        String consulta = "select n from Empresas n where n.tipoEmpresa = :tipoEmpresa";
        Query q = getEntityManager().createQuery(consulta);
        q.setParameter("tipoEmpresa", "Proveedor"); //Variable a pasar de la sesión
        return q.getResultList();
    }
 
    
}
