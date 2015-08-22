package views;

import controller.HerramientasxempcomiteFacade;
import entities.Herramientasxempcomite;
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

@Named("herramientasxempcomiteController")
@SessionScoped
public class HerramientasxempcomiteController implements Serializable {

    private Herramientasxempcomite current;
    private DataModel items = null;
    @EJB
    private controller.HerramientasxempcomiteFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Herramientasxempcomite> filtro;

    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;
    private int idU;

    public HerramientasxempcomiteController() {faceContext=FacesContext.getCurrentInstance();
        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
        idU = Integer.parseInt(httpServletRequest.getSession().getAttribute("sessionUsuario").toString());
    }
    
    public List<Herramientasxempcomite> getFiltro() {
        recreateModel();
        return filtro;
    }

    public void setFiltro(List<Herramientasxempcomite> filtro) {
        this.filtro = filtro;
    }

    public Herramientasxempcomite getSelected() {
        if (current == null) {
            current = new Herramientasxempcomite();
            selectedItemIndex = -1;
        }
        return current;
    }

    private HerramientasxempcomiteFacade getFacade() {
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
    
    public PaginationHelper getPagination(int tipoHerramienta) {
        final int tipoH = tipoHerramienta;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporTipoHerramienta(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},tipoH));
                }
            };
        }
        return pagination;
    }
    
    public PaginationHelper getPaginationM(int tipoHerramienta) {
        final int tipoH = tipoHerramienta;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporTipoHerramientaM(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},tipoH, idU));
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
        current = (Herramientasxempcomite) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Herramientasxempcomite();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String reload() {
        recreatePagination();
        recreateModel();
        return "herramientas";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HerramientasxempcomiteCreated"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",  "La herramienta ha sido adicionada con éxito");  
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return reload();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Herramientasxempcomite) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HerramientasxempcomiteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Herramientasxempcomite) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HerramientasxempcomiteDeleted"));
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
    
    public DataModel getItemsE(int tipoHerramienta) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPagination(tipoHerramienta).createPageDataModel();
        }
        return items;
    }
    
    public DataModel getItemsEM(int tipoHerramienta) {
        recreatePagination();
        recreateModel();
        if (items == null) {
            items = getPaginationM(tipoHerramienta).createPageDataModel();
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

    public Herramientasxempcomite getHerramientasxempcomite(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Herramientasxempcomite.class)
    public static class HerramientasxempcomiteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HerramientasxempcomiteController controller = (HerramientasxempcomiteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "herramientasxempcomiteController");
            return controller.getHerramientasxempcomite(getKey(value));
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
            if (object instanceof Herramientasxempcomite) {
                Herramientasxempcomite o = (Herramientasxempcomite) object;
                return getStringKey(o.getIdHerramientasxEmpComite());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Herramientasxempcomite.class.getName());
            }
        }

    }

}
