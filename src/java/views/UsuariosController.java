package views;

import controller.UsuariosFacade;
import entities.Usuarios;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

import org.primefaces.event.CloseEvent;
import views.util.JsfUtil;
import views.util.PaginationHelper;

@Named("usuariosController")
@ManagedBean
@RequestScoped
@SessionScoped
public class UsuariosController implements Serializable {

    private Usuarios current;
    private DataModel items = null;
    @EJB
    private controller.UsuariosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;

    public UsuariosController() {
        faceContext=FacesContext.getCurrentInstance();
        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
    }

    public Usuarios getSelected() {
        if (current == null) {
            current = new Usuarios();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UsuariosFacade getFacade() {
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


    public PaginationHelper getPagination(int perf) {
        final int perU = perf;
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findporPerfil(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()},perU));
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
        current = (Usuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewUsuario";
    }

    public String prepareCreate() {
        current = new Usuarios();
        selectedItemIndex = -1;
        return "/usuarios/Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuariosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String reloadMiembro (){
        recreateModel();
        return "/usuarios/DirMiembros";        
    }

    public String prepareEdit() {
        current = (Usuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String prepareEditP() {
        current = (Usuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditP";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuariosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void destroy() {
        current = (Usuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        RequestContext.getCurrentInstance().execute("PF('confirmDialog').show();");
    }

    public String destroyFinal() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El usuario ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "List";
    }

    public String destroyFinalP() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El usuario ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "LisP";
    }

    public String destroyFinal1() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El usuario ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "DirMiembros";
    }

    public String destroyFinal1P() {
        performDestroy();
        recreatePagination();
        recreateModel();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alerta",  "El usuario ha sido eliminado con exito");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "DirProveedores";
    }
    
    public String Miembros() {
        recreatePagination();
        recreateModel();
        return "/usuarios/DirMiembros";
    }
    
    public String Proveedores() {
        recreatePagination();
        recreateModel();
        return "/usuarios/DirProveedores";
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

    public String autenticar(Usuarios usu) {
        
        Usuarios usuarioBd = ejbFacade.findporLogin(usu.getUsuarioUsuario());
        if (usuarioBd != null) {
            if (usuarioBd.getContrasenaUsuario().compareTo(usu.getContrasenaUsuario()) == 0) {
                if(usuarioBd.getFkidPerfil().getIdPerfil() == 1){
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioBd.getIdUsuarios());
                    System.out.println("Empresa "+usuarioBd.getFkidPerfil());
                    return "administrador";
                }else if(usuarioBd.getFkidPerfil().getIdPerfil() == 2){
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioBd.getIdUsuarios());
                    return "comite";
                }else{
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioBd.getIdUsuarios());
                    return "proveedor";
                }
            } else {              
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta",  "Contraseña incorrecta. Intentelo de nuevo");  
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
  
        } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta",  "Usuario inexistente. Intentelo de nuevo");  
                RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        return null;

    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuariosDeleted"));
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

    public DataModel getItems (int per) {
        if (items == null) {
             items = getPagination(per).createPageDataModel();
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
    
    public String reloadMC() {
        recreateModel();
        return "/InicioMiembroComite";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItemsUsuarios(ejbFacade.findAll(), true);
    }

    public SelectItem[] getItemsAvailableSelectOneo() {
        return JsfUtil.getSelectItemsUsuarioso(ejbFacade.findAll(), true);
    }

    public SelectItem[] getItemsAvailableSelectOneM() {
        return JsfUtil.getSelectItemsUsuariosM(ejbFacade.findAllMiembro(), true);
    }

    public Usuarios getUsuarios(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Usuarios.class)
    public static class UsuariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuariosController controller = (UsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuariosController");
            return controller.getUsuarios(getKey(value));
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
            if (object instanceof Usuarios) {
                Usuarios o = (Usuarios) object;
                return getStringKey(o.getIdUsuarios());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuarios.class.getName());
            }
        }

    }

}
