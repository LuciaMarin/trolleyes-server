package net.ausiasmarch.service;

import com.google.gson.Gson;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.bean.UsuarioBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.UsuarioDao;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.setting.ConnectionSettings;

public class UsuarioService extends GenericService implements ServiceInterface {

    String[] nombre = {"Marcel·li", "Pompeu", "Cirili", "Paco",
        "Josepa", "Vidal", "Domènec", "Maurici", "Eudald", "Miqueleta", "Bernat", "Jaumet", "Pepet"};
    String[] apellido1 = {"de Cal", "el de", "de la",
        "dels", "de Can", "de les", "Ca la", "Pacoco"};
    String[] apellido2 = {"Pacoco", "Clapés",
        "Trencapins", "Palla", "Cargols", "Metge", "Murallot", "Porrons", "Cigrons", "Llobarro", "Faves", "Cebes", "Freda"};

    public UsuarioService(HttpServletRequest oRequest) {
        super(oRequest);
    }

    public String login() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean = null;
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        Gson oGson = GsonFactory.getGson();
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            UsuarioBean oUsuarioBean;
            String login = oRequest.getParameter("username");
            String password = oRequest.getParameter("password");
            oUsuarioBean = oUsuarioDao.get(login, password);

            if (oUsuarioBean != null) {
                if (oRequest.getParameter("username").equals(oUsuarioBean.getLogin()) && oRequest.getParameter("password").equalsIgnoreCase(oUsuarioBean.getPassword())) {
                    oSession.setAttribute("usuario", oRequest.getParameter("username"));
                    oResponseBean = new ResponseBean(200, "Welcome");
                } else {
                    oResponseBean = new ResponseBean(500, "Wrong password");
                }
            } else {
                oResponseBean = new ResponseBean(500, "Wrong password");
            }

            return oGson.toJson(oResponseBean);
        } catch (Exception ex) {
            String msg = this.getClass().getName() + " ob: " + ob + "; login method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }

    }

    public String check() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean = null;
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        Gson oGson = GsonFactory.getGson();
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            String usuario = (String) oSession.getAttribute("usuario");
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            UsuarioBean oUsuarioBean;
            if (usuario == null) {
                oResponseBean = new ResponseBean(500, "No autorizado");
            } else {
                oUsuarioBean = oUsuarioDao.get(usuario);
                return "{\"status\":200,\"message\":" + oGson.toJson(oUsuarioBean) + "}";
            }

        } catch (Exception ex) {
            String msg = this.getClass().getName() + " ob: " + ob + "; check method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
        return oGson.toJson(oResponseBean);
    }

    public String logout() {
        HttpSession oSession = oRequest.getSession();
        oSession.invalidate();
        ResponseBean oResponseBean = null;
        oResponseBean = new ResponseBean(200, "No active session");
        Gson oGson = GsonFactory.getGson();
        return oGson.toJson(oResponseBean);
    }

    public String fill() throws Exception {
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        Gson oGson = GsonFactory.getGson();
        ResponseBean oResponseBean = null;
        try {
            oConnectionImplementation = ConnectionFactory
                    .getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);

            int numUsuario = Integer.parseInt(oRequest.getParameter("number"));
            for (int i = 0; i < numUsuario; i++) {
                UsuarioBean oUsuarioBean = new UsuarioBean();
                oUsuarioBean.setDni((int) Math.floor(Math.random() * (10000000 - 99999999) + 99999999) + "O");
                String nombrePersona = nombre[(int) (Math.random() * nombre.length) + 0];
                String apellido1Persona = apellido1[(int) (Math.random() * apellido1.length) + 0];
                String apellido2Persona = apellido2[(int) (Math.random() * apellido2.length) + 0];
                String username = nombrePersona.substring(0, 2).toLowerCase().trim()
                        + apellido1Persona.substring(0, 2).toLowerCase().trim()
                        + apellido2Persona.substring(0, 2).toLowerCase().trim()
                        + (int) Math.floor(Math.random() * (1000 - 9999) + 9999);
                oUsuarioBean.setNombre(nombrePersona);
                oUsuarioBean.setApellido1(apellido1Persona);
                oUsuarioBean.setApellido2(apellido2Persona);
                oUsuarioBean.setEmail(username + "@trolleyes.com");
                oUsuarioBean.setLogin(username);
                oUsuarioBean.setPassword("da8ab09ab4889c6208116a675cad0b13e335943bd7fc418782d054b32fdfba04");
                oUsuarioBean.setTipo_usuario_id(2);
                oUsuarioDao.insert(oUsuarioBean);
            }
            oResponseBean = new ResponseBean(200, "Insertados los registros con exito");
        } catch (Exception ex) {
            String msg = this.getClass().getName() + " ob: " + ob + "; check method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
        return oGson.toJson(oResponseBean);
    }

}
