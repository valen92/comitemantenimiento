package views;

import controller.RepuestosxempresasFacade;
import entities.Repuestosxempresas;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
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

@Named("repuestosxempresasController")
@SessionScoped
public class RepuestosxempresasController implements Serializable {

    private Repuestosxempresas current;
    private DataModel items = null;
    @EJB
    private controller.RepuestosxempresasFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Repuestosxempresas> filtro;

    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;
    private int idU;

    public RepuestosxempresasController() {
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
    
    public List<Repuestosxempresas> getFiltro() {
        recreateModel();
        return filtro;
    }

    public void setFiltro(List<Repuestosxempresas> filtro) {
        this.filtro = filtro;
    }

    public Repuestosxempresas getSelected() {
        if (current == null) {
            current = new Repuestosxempresas();
            selectedItemIndex = -1;
        }
        return current;
    }

    private RepuestosxempresasFacade getFacade() {
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
    
    public PaginationHelper getPagination(int tipoRepuesto) {
        final int tipoR = tipoRepuesto;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporTipoRepuesto(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},tipoR));
                }
            };
        }
        return pagination;
    }
    
    public PaginationHelper getPaginationU(int tipoRepuesto) {
        final int tipoR = tipoRepuesto;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporTipoRepuestoU(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},tipoR, idU));
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
        current = (Repuestosxempresas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Repuestosxempresas();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String reload() {
        recreatePagination();
        recreateModel();
        return "Repuestos";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RepuestosxempresasCreated"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",  "El repuesto ha sido adicionado con éxito");  
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return reload();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Repuestosxempresas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RepuestosxempresasUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Repuestosxempresas) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RepuestosxempresasDeleted"));
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

    public DataModel getItems(int tipoRepuesto) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPagination(tipoRepuesto).createPageDataModel();
        }
        return items;
    }

    public DataModel getItemsU(int tipoRepuesto) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationU(tipoRepuesto).createPageDataModel();
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

    public SelectItem[] getItemsAvailableSelectOneM() {
        return JsfUtil.getSelectItemsCodigoRepuesto(ejbFacade.findAll(), true);
    }

    public Repuestosxempresas getRepuestosxempresas(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Repuestosxempresas.class)
    public static class RepuestosxempresasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RepuestosxempresasController controller = (RepuestosxempresasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "repuestosxempresasController");
            return controller.getRepuestosxempresas(getKey(value));
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
            if (object instanceof Repuestosxempresas) {
                Repuestosxempresas o = (Repuestosxempresas) object;
                return getStringKey(o.getIdRepuestosxEmpresas());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Repuestosxempresas.class.getName());
            }
        }

    }

}
