package views;

import controller.ObservacionesproveedorFacade;
import entities.Observacionesproveedor;
import entities.Servicioscontrato;
import java.io.Serializable;
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
import org.primefaces.context.RequestContext;
import views.util.JsfUtil;
import views.util.PaginationHelper;

@Named("observacionesproveedorController")
@SessionScoped
public class ObservacionesproveedorController implements Serializable {

    private Observacionesproveedor current;
    private DataModel items = null;
    @EJB
    private controller.ObservacionesproveedorFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int idServiciosContrato;
    private int idUsuario;

    public ObservacionesproveedorController() {
    }

    public Observacionesproveedor getSelected() {
        if (current == null) {
            current = new Observacionesproveedor();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ObservacionesproveedorFacade getFacade() {
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
                    return new ListDataModel(getFacade().findporUsuario(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idUsuario,idServiciosContrato));
                }
            };
        }
        return pagination;
    }

    public String prepareListOb(int id, int idU) {
        idServiciosContrato=id;
        idUsuario=idU;
        recreateModel();
        return "/observacionesproveedor/Observaciones";
    }

    public String prepareListObM(int id, int idU) {
        idServiciosContrato=id;
        idUsuario=idU;
        recreateModel();
        return "/observacionesproveedor/ObservacionesM";
    }

    public String prepareList() {
        recreateModel();
        return "/observacionesproveedor/Observaciones";
    }

    public String prepareListM() {
        recreateModel();
        return "/observacionesproveedor/ObservacionesM";
    }

    public String prepareView() {
        current = (Observacionesproveedor) getItemsd().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewM";
    }

    public String prepareCreate() {
        current = new Observacionesproveedor();
        selectedItemIndex = -1;
        return "/observacionesproveedor/CrearObservacion";
    }

    public String prepareCreateM() {
        current = new Observacionesproveedor();
        selectedItemIndex = -1;
        return "/observacionesproveedor/CrearObservacionM";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionesproveedorCreated"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",  "La observación ha sido adicionada con éxito");  
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "Observaciones";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String createM() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionesproveedorCreated"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",  "La observación ha sido adicionada con éxito");  
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "ObservacionesM";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Observacionesproveedor) getItemsd().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionesproveedorUpdated"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La observación ha sido actualizada con éxito");  
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }public void destroy() {
        current = (Observacionesproveedor) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        RequestContext.getCurrentInstance().execute("PF('confirmDialog').show();");
    }

    public String destroyFinal() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La observación ha sido eliminada con éxito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "List";
    }

    public String destroyFinal1() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La observación ha sido eliminada con éxito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "Observaciones";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ObservacionesproveedorDeleted"));
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
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public DataModel getItemsd() {
        if (items == null) {
            items = getPagination().createPageDataModel();
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

    public Observacionesproveedor getObservacionesproveedor(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Observacionesproveedor.class)
    public static class ObservacionesproveedorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ObservacionesproveedorController controller = (ObservacionesproveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "observacionesproveedorController");
            return controller.getObservacionesproveedor(getKey(value));
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
            if (object instanceof Observacionesproveedor) {
                Observacionesproveedor o = (Observacionesproveedor) object;
                return getStringKey(o.getIdObservacionesProveedor());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Observacionesproveedor.class.getName());
            }
        }

    }

}
