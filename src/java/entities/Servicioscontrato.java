/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alexandra
 */
@Entity
@Table(name = "servicioscontrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicioscontrato.findAll", query = "SELECT s FROM Servicioscontrato s"),
    @NamedQuery(name = "Servicioscontrato.findByIdServiciosContrato", query = "SELECT s FROM Servicioscontrato s WHERE s.idServiciosContrato = :idServiciosContrato")})

public class Servicioscontrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idServiciosContrato")
    private Integer idServiciosContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkidServiciosContrato")
    private List<Observacionesproveedor> observacionesproveedorList;
    @JoinColumn(name = "Fk_idContratos", referencedColumnName = "idContratos")
    @ManyToOne(optional = false)
    private Contratos fkidContratos;
    @JoinColumn(name = "Fk_idServiciosxEmpProveedoras", referencedColumnName = "idServiciosxEmpProveedoras")
    @ManyToOne(optional = false)
    private Serviciosxempproveedoras fkidServiciosxEmpProveedoras;
    @JoinColumn(name = "Fk_idEmpresas", referencedColumnName = "idEmpresas")
    @OneToOne(optional = false)
    private Empresas fkidEmpresas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkidServiciosContrato")
    private List<Actividades> actividadesList;

    public Servicioscontrato() {
    }

    public Servicioscontrato(Integer idServiciosContrato) {
        this.idServiciosContrato = idServiciosContrato;
    }

    public Integer getIdServiciosContrato() {
        return idServiciosContrato;
    }

    public void setIdServiciosContrato(Integer idServiciosContrato) {
        this.idServiciosContrato = idServiciosContrato;
    }

    @XmlTransient
    public List<Observacionesproveedor> getObservacionesproveedorList() {
        return observacionesproveedorList;
    }

    public void setObservacionesproveedorList(List<Observacionesproveedor> observacionesproveedorList) {
        this.observacionesproveedorList = observacionesproveedorList;
    }

    public Contratos getFkidContratos() {
        return fkidContratos;
    }

    public void setFkidContratos(Contratos fkidContratos) {
        this.fkidContratos = fkidContratos;
    }

    public Serviciosxempproveedoras getFkidServiciosxEmpProveedoras() {
        return fkidServiciosxEmpProveedoras;
    }

    public void setFkidServiciosxEmpProveedoras(Serviciosxempproveedoras fkidServiciosxEmpProveedoras) {
        this.fkidServiciosxEmpProveedoras = fkidServiciosxEmpProveedoras;
    }

    public Empresas getFkidEmpresas() {
        return fkidEmpresas;
    }

    public void setFkidEmpresas(Empresas fkidEmpresas) {
        this.fkidEmpresas = fkidEmpresas;
    }

    @XmlTransient
    public List<Actividades> getActividadesList() {
        return actividadesList;
    }

    public void setActividadesList(List<Actividades> actividadesList) {
        this.actividadesList = actividadesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServiciosContrato != null ? idServiciosContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicioscontrato)) {
            return false;
        }
        Servicioscontrato other = (Servicioscontrato) object;
        if ((this.idServiciosContrato == null && other.idServiciosContrato != null) || (this.idServiciosContrato != null && !this.idServiciosContrato.equals(other.idServiciosContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + idServiciosContrato ;
    }

}
