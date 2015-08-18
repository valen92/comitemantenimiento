package views;

import entities.Actividades;
import views.util.JsfUtil;
import views.util.PaginationHelper;
import controller.ActividadesFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@Named("actividadesController")
@SessionScoped
public class ActividadesController implements Serializable {

    private Actividades current;
    private DataModel items = null;
    @EJB
    private controller.ActividadesFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int idU;
    private final FacesContext faceContext;
    private final HttpServletRequest httpServletRequest;

    public ActividadesController() {
        faceContext=FacesContext.getCurrentInstance();
        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
        idU = Integer.parseInt(httpServletRequest.getSession().getAttribute("sessionUsuario").toString());
    }

    public Actividades getSelected() {
        if (current == null) {
            current = new Actividades();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ActividadesFacade getFacade() {
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
                    return new ListDataModel(getFacade().findActividad(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, idU));
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
        current = (Actividades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/actividades/View";
    }

    public String prepareViewP() {
        current = (Actividades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/actividades/ConsultaActividad";
    }

    public String prepareCreate() {
        current = new Actividades();
        selectedItemIndex = -1;
        return "/actividades/CrearActividad";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadesCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Actividades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadesUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void destroy() {
        current = (Actividades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        RequestContext.getCurrentInstance().execute("PF('confirmDialog').show();");
    }

    public String destroyFinal() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La actividad ha sido eliminada con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "/InicioMiembroComite_1";
    }

    public String destroyFinal1() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La actividad ha sido eliminada con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "/InicioMiembroComite";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadesDeleted"));
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

    public Actividades getActividades(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Actividades.class)
    public static class ActividadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActividadesController controller = (ActividadesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "actividadesController");
            return controller.getActividades(getKey(value));
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
            if (object instanceof Actividades) {
                Actividades o = (Actividades) object;
                return getStringKey(o.getIdActividad());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Actividades.class.getName());
            }
        }

    }

}
