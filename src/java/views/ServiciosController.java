package views;

import controller.ServiciosFacade;
import entities.Actividades;
import entities.Servicios;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import views.util.JsfUtil;
import views.util.PaginationHelper;

@Named("serviciosController")
@SessionScoped
public class ServiciosController implements Serializable {

    private Servicios current;
    private DataModel items = null;
    @EJB
    private controller.ServiciosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;
    private int idU;
    private List<Actividades> filtro;

    public ServiciosController() {
        faceContext=FacesContext.getCurrentInstance();
        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
        try {
            idU = Integer.parseInt(httpServletRequest.getSession().getAttribute("sessionUsuario").toString());
        } catch( NullPointerException e ) {
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",  "La sesión ha caducado. "
                + "Por favor inice sesión nuevamente");  
             RequestContext.getCurrentInstance().showMessageInDialog(message);
             logout();
        }
    }
    
    public String logout (){
        return "login";
    }
    
    public List<Actividades> getFiltro() {
        recreateModel();
        return filtro;
    }

    public void setFiltro(List<Actividades> filtro) {
        this.filtro = filtro;
    }

    public Servicios getSelected() {
        if (current == null) {
            current = new Servicios();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ServiciosFacade getFacade() {
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
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationServicio(int servicio) {
        final int idServicio=servicio;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findActividadServicio(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, idU, idServicio));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationAsociadas(int servicio) {
        final int idServicio=servicio;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findAsociadas(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, idU, idServicio));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationFrecuencia(int servicio) {
        final int idServicio=servicio;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findFrecuencia(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, idU, idServicio));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Servicios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Servicios();
        selectedItemIndex = -1;
        return "servicios/CrearServicio";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServiciosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Servicios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditServicio";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServiciosUpdated"));
            return "servicios/ListServicios";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void destroy() {
        current = (Servicios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        RequestContext.getCurrentInstance().execute("PF('confirmDialog').show();");
    }

    public String destroyFinal() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El servicio ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyFinal1() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El servicio ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListServicios";
    }
    
    public String reload() {
        recreateModel();
        return "ListServicios";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ServiciosDeleted"));
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
    
    public DataModel getItemsServicio(int servicio) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationServicio(servicio).createPageDataModel();
        }
        return items;
    }

    public DataModel getItemsAsociadas(int servicio) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationAsociadas(servicio).createPageDataModel();
        }
        return items;
    }

    public DataModel getItemsFrecuencia(int servicio) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationFrecuencia(servicio).createPageDataModel();
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
        return JsfUtil.getSelectItemsServicios(ejbFacade.findAll(), true);
    }

    public Servicios getServicios(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Servicios.class)
    public static class ServiciosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ServiciosController controller = (ServiciosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "serviciosController");
            return controller.getServicios(getKey(value));
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
            if (object instanceof Servicios) {
                Servicios o = (Servicios) object;
                return getStringKey(o.getIdServicios());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Servicios.class.getName());
            }
        }

    }

}
