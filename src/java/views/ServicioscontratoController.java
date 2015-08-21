package views;

import controller.ServicioscontratoFacade;
import entities.Empresas;
import entities.Servicioscontrato;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import views.util.JsfUtil;
import views.util.PaginationHelper;

@Named("servicioscontratoController")
@SessionScoped
public class ServicioscontratoController implements Serializable {

    private Servicioscontrato current;
    private DataModel items = null;
    @EJB
    private controller.ServicioscontratoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Servicioscontrato> getResultList;
    private int idEmpresa;
    private int idUsuario;

    public ServicioscontratoController() {
    }

    public Servicioscontrato getSelected() {
        if (current == null) {
            current = new Servicioscontrato();
            selectedItemIndex = -1;
        }
        System.out.println("currentalexandra "+current.toString());
        return current;
    }

    private ServicioscontratoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().obtenerTodosLosServicios());
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationS() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporEmpresa(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idEmpresa));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationSA() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporServicio(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idEmpresa));
                }
            };
        }
        return pagination;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public PaginationHelper getPaginationServicios() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporProActual(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idEmpresa));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationEmpresa() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporEmpresas(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idEmpresa));
                }
            };
        }
        return pagination;
    }

    public String prepareList(Empresas id, int idU) {
        idEmpresa=id.getIdEmpresas();
        idUsuario=idU;
        recreateModel();
        return "/servicioscontrato/DetalleProveedor";
    }

    public String prepareListM(Empresas id, int idU) {
        idEmpresa=id.getIdEmpresas();
        idUsuario=idU;
        recreateModel();
        return "/servicioscontrato/DetalleProveedorMC";
    }

    public void detalle(Empresas id) {
        idEmpresa=id.getIdEmpresas();
        recreateModel();
        RequestContext.getCurrentInstance().openDialog("/servicioscontrato/DetalleServicios");
    }

    public String prepareListM(Empresas id) {
        idEmpresa=id.getIdEmpresas();
        recreateModel();
        return "/servicioscontrato/DetalleProveedor";
    }

    public void prepareListMC(Empresas id) {
        idEmpresa=id.getIdEmpresas();
        recreateModel();
        RequestContext.getCurrentInstance().openDialog("/servicioscontrato/DetalleProveedorM");
    }
    

    public String prepareList() {
        recreateModel();
        return "/servicioscontrato/DetalleProveedor";
    }

    public String prepareView() {
        current = (Servicioscontrato) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Servicioscontrato();
        selectedItemIndex = -1;
        return "detalleProveedor";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServicioscontratoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Servicioscontrato) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServicioscontratoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Servicioscontrato) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServicioscontratoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    
    
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    
    public DataModel getItemsS() {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationS().createPageDataModel();
        }
        return items;
    }
    
    public DataModel getItemsEmpresa() {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationEmpresa().createPageDataModel();
        }
        return items;
    }
    
    public DataModel getItemsServicios() {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationServicios().createPageDataModel();
        }
        return items;
    }
    
    public DataModel getItemsSA() {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationSA().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    
    
    public Servicioscontrato getServicioscontrato(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Servicioscontrato.class)
    public static class ServicioscontratoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ServicioscontratoController controller = (ServicioscontratoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "servicioscontratoController");
            return controller.getServicioscontrato(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Servicioscontrato) {
                Servicioscontrato o = (Servicioscontrato) object;
                return getStringKey(o.getIdServiciosContrato());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Servicioscontrato.class.getName());
            }
        }

    }

}
