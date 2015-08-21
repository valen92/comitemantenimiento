/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexandra
 */
@Entity
@Table(name = "repuestosxempresas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repuestosxempresas.findAll", query = "SELECT r FROM Repuestosxempresas r"),
    @NamedQuery(name = "Repuestosxempresas.findByIdRepuestosxEmpresas", query = "SELECT r FROM Repuestosxempresas r WHERE r.idRepuestosxEmpresas = :idRepuestosxEmpresas"),
    @NamedQuery(name = "Repuestosxempresas.findByEstadoRepuestoempresa", query = "SELECT r FROM Repuestosxempresas r WHERE r.estadoRepuestoempresa = :estadoRepuestoempresa"),
    @NamedQuery(name = "Repuestosxempresas.findByCodigoRepuestoempresa", query = "SELECT r FROM Repuestosxempresas r WHERE r.codigoRepuestoempresa = :codigoRepuestoempresa"),
    @NamedQuery(name = "Repuestosxempresas.findByCantidadRepuestoempresa", query = "SELECT r FROM Repuestosxempresas r WHERE r.cantidadRepuestoempresa = :cantidadRepuestoempresa"),
    @NamedQuery(name = "Repuestosxempresas.findByPrecioRepuestoepresa", query = "SELECT r FROM Repuestosxempresas r WHERE r.precioRepuestoepresa = :precioRepuestoepresa"),
    @NamedQuery(name = "Repuestosxempresas.findByDescripcionRepuestoempresa", query = "SELECT r FROM Repuestosxempresas r WHERE r.descripcionRepuestoempresa = :descripcionRepuestoempresa")})
public class Repuestosxempresas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRepuestosxEmpresas")
    private Integer idRepuestosxEmpresas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "estado_repuestoempresa")
    private String estadoRepuestoempresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo_repuestoempresa")
    private int codigoRepuestoempresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_repuestoempresa")
    private int cantidadRepuestoempresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_repuestoepresa")
    private int precioRepuestoepresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion_repuestoempresa")
    private String descripcionRepuestoempresa;
    @JoinColumn(name = "Fk_idRepuestos", referencedColumnName = "idRepuestos")
    @ManyToOne(optional = false)
    private Repuestos fkidRepuestos;
    @JoinColumn(name = "Fk_idUsuarios", referencedColumnName = "idUsuarios")
    @ManyToOne(optional = false)
    private Usuarios fkidUsuarios;
    @JoinColumn(name = "Fk_idEmpresas", referencedColumnName = "idEmpresas")
    @ManyToOne(optional = false)
    private Empresas fkidEmpresas;
    @JoinColumn(name = "Fk_idMarcasRepuestos", referencedColumnName = "idMarcasRepuestos")
    @ManyToOne(optional = false)
    private Marcasrepuestos fkidMarcasRepuestos;

    public Repuestosxempresas() {
    }

    public Repuestosxempresas(Integer idRepuestosxEmpresas) {
        this.idRepuestosxEmpresas = idRepuestosxEmpresas;
    }

    public Repuestosxempresas(Integer idRepuestosxEmpresas, String estadoRepuestoempresa, int codigoRepuestoempresa, int cantidadRepuestoempresa, int precioRepuestoepresa, String descripcionRepuestoempresa) {
        this.idRepuestosxEmpresas = idRepuestosxEmpresas;
        this.estadoRepuestoempresa = estadoRepuestoempresa;
        this.codigoRepuestoempresa = codigoRepuestoempresa;
        this.cantidadRepuestoempresa = cantidadRepuestoempresa;
        this.precioRepuestoepresa = precioRepuestoepresa;
        this.descripcionRepuestoempresa = descripcionRepuestoempresa;
    }

    public Integer getIdRepuestosxEmpresas() {
        return idRepuestosxEmpresas;
    }

    public void setIdRepuestosxEmpresas(Integer idRepuestosxEmpresas) {
        this.idRepuestosxEmpresas = idRepuestosxEmpresas;
    }

    public String getEstadoRepuestoempresa() {
        return estadoRepuestoempresa;
    }

    public void setEstadoRepuestoempresa(String estadoRepuestoempresa) {
        this.estadoRepuestoempresa = estadoRepuestoempresa;
    }

    public int getCodigoRepuestoempresa() {
        return codigoRepuestoempresa;
    }

    public void setCodigoRepuestoempresa(int codigoRepuestoempresa) {
        this.codigoRepuestoempresa = codigoRepuestoempresa;
    }

    public int getCantidadRepuestoempresa() {
        return cantidadRepuestoempresa;
    }

    public void setCantidadRepuestoempresa(int cantidadRepuestoempresa) {
        this.cantidadRepuestoempresa = cantidadRepuestoempresa;
    }

    public int getPrecioRepuestoepresa() {
        return precioRepuestoepresa;
    }

    public void setPrecioRepuestoepresa(int precioRepuestoepresa) {
        this.precioRepuestoepresa = precioRepuestoepresa;
    }

    public String getDescripcionRepuestoempresa() {
        return descripcionRepuestoempresa;
    }

    public void setDescripcionRepuestoempresa(String descripcionRepuestoempresa) {
        this.descripcionRepuestoempresa = descripcionRepuestoempresa;
    }

    public Repuestos getFkidRepuestos() {
        return fkidRepuestos;
    }

    public void setFkidRepuestos(Repuestos fkidRepuestos) {
        this.fkidRepuestos = fkidRepuestos;
    }

    public Usuarios getFkidUsuarios() {
        return fkidUsuarios;
    }

    public void setFkidUsuarios(Usuarios fkidUsuarios) {
        this.fkidUsuarios = fkidUsuarios;
    }

    public Empresas getFkidEmpresas() {
        return fkidEmpresas;
    }

    public void setFkidEmpresas(Empresas fkidEmpresas) {
        this.fkidEmpresas = fkidEmpresas;
    }

    public Marcasrepuestos getFkidMarcasRepuestos() {
        return fkidMarcasRepuestos;
    }

    public void setFkidMarcasRepuestos(Marcasrepuestos fkidMarcasRepuestos) {
        this.fkidMarcasRepuestos = fkidMarcasRepuestos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRepuestosxEmpresas != null ? idRepuestosxEmpresas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repuestosxempresas)) {
            return false;
        }
        Repuestosxempresas other = (Repuestosxempresas) object;
        if ((this.idRepuestosxEmpresas == null && other.idRepuestosxEmpresas != null) || (this.idRepuestosxEmpresas != null && !this.idRepuestosxEmpresas.equals(other.idRepuestosxEmpresas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + idRepuestosxEmpresas + "";
    }
    public String toStringCodigo() {
        return "" + codigoRepuestoempresa + "";
    }
    
}
