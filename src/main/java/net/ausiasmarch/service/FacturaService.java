package net.ausiasmarch.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletRequest;
import net.ausiasmarch.bean.FacturaBean;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.FacturaDao;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.setting.ConnectionSettings;

public class FacturaService extends GenericService implements ServiceInterface {

    public FacturaService(HttpServletRequest oRequest) {
        super(oRequest);
    }

    public String fill() throws Exception {
         ConnectionInterface oConnectionImplementation = null;
         Connection oConnection = null;
         ResponseBean oResponseBean = null;
         Gson oGson = GsonFactory.getGson();
           try {
        oConnectionImplementation = ConnectionFactory
                .getConnection(ConnectionSettings.connectionPool);
        oConnection = oConnectionImplementation.newConnection();
        FacturaDao oFacturaDao = new FacturaDao(oConnection);
        Date date1 = new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime();
        Date date2 = new GregorianCalendar(2019, Calendar.DECEMBER, 31).getTime();
        int numFactura = Integer.parseInt(oRequest.getParameter("number"));
        for (int i = 0; i < numFactura; i++) {
            FacturaBean oFacturaBean = new FacturaBean();
            Date randomDate = new Date(ThreadLocalRandom.current().nextLong(date1.getTime(), date2.getTime()));
            oFacturaBean.setIva(21);
            oFacturaBean.setFecha(randomDate);
            oFacturaBean.setUsuario_id((int) (Math.random() * 25) + 2);
            oFacturaDao.insert(oFacturaBean);
        }
        oResponseBean = new ResponseBean(200, "Insertados los registros con exito");
           } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
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
