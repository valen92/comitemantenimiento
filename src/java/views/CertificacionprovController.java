package views;

import entities.Certificacionprov;
import views.util.JsfUtil;
import views.util.PaginationHelper;
import controller.CertificacionprovFacade;

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
import org.primefaces.context.RequestContext;

@Named("certificacionprovController")
@SessionScoped
public class CertificacionprovController implements Serializable {

    private Certificacionprov current;
    private DataModel items = null;
    @EJB
    private controller.CertificacionprovFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int idEmpresa;

    public CertificacionprovController() {
    }

    public Certificacionprov getSelected() {
        if (current == null) {
            current = new Certificacionprov();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CertificacionprovFacade getFacade() {
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
                    return new ListDataModel(getFacade().findCertificacion(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},idEmpresa));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareVista(int id) {
        idEmpresa=id;
        recreateModel();
        return "/certificacionprov/VerCertificaciones";
    }

    public String prepareView() {
        current = (Certificacionprov) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Certificacionprov();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CertificacionprovCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Certificacionprov) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CertificacionprovUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void destroy() {
        current = (Certificacionprov) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        RequestContext.getCurrentInstance().execute("PF('confirmDialog').show();");
    }

    public String destroyFinal() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La certificación ha sido eliminada con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "List";
    }

    public String destroyFinal1() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "La certificación ha sido eliminada con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "VerCertificaciones";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CertificacionprovDeleted"));
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

    public Certificacionprov getCertificacionprov(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Certificacionprov.class)
    public static class CertificacionprovControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CertificacionprovController controller = (CertificacionprovController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "certificacionprovController");
            return controller.getCertificacionprov(getKey(value));
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
            if (object instanceof Certificacionprov) {
                Certificacionprov o = (Certificacionprov) object;
                return getStringKey(o.getIdCertificacionProv());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Certificacionprov.class.getName());
            }
        }

    }

}
