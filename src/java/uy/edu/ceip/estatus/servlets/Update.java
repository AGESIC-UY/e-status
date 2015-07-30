/*
 *  Copyright 2015 Consejo de Educación Inicial y Primaria - Administración Nacional de Educación Pública - Uruguay
 *
 *  This file is part of e-status.
 *
 *  e-status is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  e-status is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with e-status.  If not, see <http://www.gnu.org/licenses/>.
 */
package uy.edu.ceip.estatus.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uy.edu.ceip.estatus.Indicator;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class Update extends HttpServlet {
    
    private static final String UPDATE_LOG = "Indicator \"%1$s\" (update frequency is %2$s) was updated on %3$tY-%3$tm-%3$tH %3$tk:%3$tM:%3$tS: must update.%n";
    private static final String DONT_UPDATE_LOG = "Indicator \"%1$s\" (update frequency is %2$s) was updated on %3$tY-%3$tm-%3$tH %3$tk:%3$tM:%3$tS: must not update.%n";
    private static final String LOG_PREFIX = "Executing automatic update...\n";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection localConnection = null;
        Connection guriConnection = null;
        StringBuilder log = new StringBuilder(Update.UPDATE_LOG.length() * 10);
        try {
            Date current = new Date();
            localConnection = ConnectionFactory.getLocalEstatusConnection();
            Statement localStatement = localConnection.createStatement();
            //Check what indicators need to be updated
            ResultSet localResult = localStatement.executeQuery("SELECT id, name, query, update_interval, taken FROM indicator LEFT JOIN data d1 ON d1.indicator = indicator.id WHERE active AND (d1.taken IS NULL OR d1.taken = (SELECT MAX(taken) FROM data d2 WHERE d1.indicator = d2.indicator));");
            ArrayList<Indicator> indicatorsToUpdate = new ArrayList<>();
            log.append(Update.LOG_PREFIX);
            log.append(String.format("Current time: %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%n", current));
            while (localResult.next()) {
                Indicator i = new Indicator(localResult.getInt("id"), localResult.getString("name"), localResult.getString("query"), localResult.getObject("taken") != null ? localResult.getTimestamp("taken") : null, localResult.getInt("update_interval"));
                if (i.mustUpdate(current)) {
                    indicatorsToUpdate.add(i);
                    log.append(String.format(Update.UPDATE_LOG, i.getName(), i.getUpdateInterval(), i.getLastUpdated()));
                } else {
                    log.append(String.format(Update.DONT_UPDATE_LOG, i.getName(), i.getUpdateInterval(), i.getLastUpdated()));
                }
            }
            //<editor-fold defaultstate="collapsed" desc="Get a connection to production db">
            guriConnection = ConnectionFactory.getGURIConnection();
            Statement guriStatement = guriConnection.createStatement();
            ResultSet guriResult;
            //</editor-fold>
            for (Indicator i : indicatorsToUpdate) {
                guriResult = guriStatement.executeQuery(i.getQuery());
                guriResult.next();
                localStatement.executeUpdate(String.format("INSERT INTO data(indicator, taken, val) VALUES ('%1$s', '%2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS'::TIMESTAMP, '%3$s');", i.getId(), current, guriResult.getString(1)));
            }
            Logging.getInitializedLogger().log(Level.CONFIG, log.toString());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            Logging.getInitializedLogger().log(Level.SEVERE, Logging.buildMessage((String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()), ex));
            ServletException servletEx = new ServletException(ex);
            servletEx.setStackTrace(ex.getStackTrace());
            throw servletEx;
        } finally {
            if (localConnection != null) {
                try {
                    localConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (guriConnection != null) {
                try {
                    guriConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
