package views.util;

import entities.Ciudad;
import entities.Claserepuestos;
import entities.Contratos;
import entities.Departamento;
import entities.Empresas;
import entities.Herramientas;
import entities.Linearepuestos;
import entities.Marcasherramientas;
import entities.Marcasrepuestos;
import entities.Perfil;
import entities.Repuestos;
import entities.Servicios;
import entities.Serviciosxempproveedoras;
import entities.Tipoherramientas;
import entities.Usuarios;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un número de contrato");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
    
    
     public static SelectItem[] getSelectItemsServicios(List<Servicios> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un servicio");
            i++;
        }
        for (Servicios x : entities) {
            items[i++] = new SelectItem(x, x.getNombreServicio());
        }
        return items;
    }
     
     public static SelectItem[] getSelectItemsCiudad(List<Ciudad> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un ciudad");
            i++;
        }
        for (Ciudad x : entities) {
            items[i++] = new SelectItem(x, x.getNombreCiudad());
        }
        return items;
    } 
     
    public static SelectItem[] getSelectItemsEmpresas(List<Empresas> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Empresas registradas");
            i++;
        }
        for (Empresas x : entities) {
            items[i++] = new SelectItem(x, x.getNombreEmpresa());
        }
        return items;
    } 
    
    
    
    public static SelectItem[] getSelectItemsDepartamento(List<Departamento> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un departamento");
            i++;
        }
        for (Departamento x : entities) {
            items[i++] = new SelectItem(x, x.getNombreDepartamento());
        }
        return items;
    } 
    
    public static SelectItem[] getSelectItemsTipoHerramienta(List<Tipoherramientas> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una herramienta");
            i++;
        }
        for (Tipoherramientas x : entities) {
            items[i++] = new SelectItem(x, x.getNombreTipoherramienta());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsMarcasHerramientas(List<Marcasherramientas> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una marca");
            i++;
        }
        for (Marcasherramientas x : entities) {
            items[i++] = new SelectItem(x, x.getNombreMarcaherramienta());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsUsuarios(List<Usuarios> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un usuario");
            i++;
        }
        for (Usuarios x : entities) {
            items[i++] = new SelectItem(x, x.getNombreUsuario());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsUsuarioso(List<Usuarios> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un usuario");
            i++;
        }
        for (Usuarios x : entities) {
            items[i++] = new SelectItem(x, x.getNombreUsuario()+" "+x.getApellidoUsuario());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsUsuariosM(List<Usuarios> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una empresa");
            i++;
        }
        for (Usuarios x : entities) {
            items[i++] = new SelectItem(x.getFkidEmpresas().getNombreEmpresa(), x.getFkidEmpresas().getNombreEmpresa());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsUsuariosP(List<Usuarios> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una empresa");
            i++;
        }
        for (Usuarios x : entities) {
            items[i++] = new SelectItem(x.getFkidEmpresas().getNombreEmpresa(), x.getFkidEmpresas().getNombreEmpresa());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsHerramientas(List<Herramientas> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una herramienta");
            i++;
        }
        for (Herramientas x : entities) {
            items[i++] = new SelectItem(x, x.getNombreHerramienta());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsLineaRepuestos(List<Linearepuestos> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una línea");
            i++;
        }
        for (Linearepuestos x : entities) {
            items[i++] = new SelectItem(x, x.getNombreLinearepuesto());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsClaseRepuestos(List<Claserepuestos> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una clase");
            i++;
        }
        for (Claserepuestos x : entities) {
            items[i++] = new SelectItem(x, x.getNombreClaserepuesto());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsRepuestos(List<Repuestos> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un repuesto");
            i++;
        }
        for (Repuestos x: entities) {
            items[i++] = new SelectItem(x, x.getNombreRepuesto());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsContratos(List<Contratos> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione contrato");
            i++;
        }
        for (Contratos x: entities) {
            items[i++] = new SelectItem(x, x.getDescripcionContrato());
        }
        return items;
    }
    
    public static SelectItem[] getSelectItemsMarcasRepuestos(List<Marcasrepuestos> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione una marca");
            i++;
        }
        for (Marcasrepuestos x: entities) {
            items[i++] = new SelectItem(x, x.getNombreMarcarepuesto());
        }
        return items;
    }

    public static SelectItem[] getSelectItemsPerfil(List<Perfil> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione un perfil");
            i++;
        }
        for (Perfil x: entities) {
            items[i++] = new SelectItem(x, x.getNombrePerfil());
        }
        return items;
    }
    
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

}
