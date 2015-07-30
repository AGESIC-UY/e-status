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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.db.DBUtils;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class Admin extends HttpServlet {
    
    private static final String ACTION_UPDATE_REL_USER_INDICATOR = "u";
    private static final String DELETE_REL_USER_INDICATOR = "DELETE FROM rel_user_indicator;";
    private static final String INSERT_REL_USER_INDICATOR = "INSERT INTO rel_user_indicator VALUES ";
    private static final String INSERT_REL_USER_INDICATOR_SUFFIX = " (?, ?) ";
    private static final String USERS_QUERY = "SELECT * FROM local_user ORDER BY id";
    private static final String INDICATORS_QUERY = "SELECT id, \"name\", explanation, active FROM indicator ORDER BY id";
    private static final String REL_USER_INDICATOR_QUERY = "SELECT rel_user_indicator.* FROM rel_user_indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id ORDER BY localuser, indicator;";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        List<String[]> users = new ArrayList<>();
        List<String[]> indicators = new ArrayList<>();
        boolean[][] relUserIndicator;
        try {
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(Admin.USERS_QUERY);
            while (result.next()) {
                users.add(new String[] {result.getString("id"), result.getString("local_user_name"), result.getString("local_user_long_name")});
            }
            result.close();
            result = statement.executeQuery(Admin.INDICATORS_QUERY);
            while (result.next()) {
                indicators.add(new String[] {result.getString(1), result.getString(2), result.getString(3), String.valueOf(result.getBoolean(4))});
            }
            result.close();
            relUserIndicator = new boolean[users.size()][indicators.size()];
            result = statement.executeQuery(Admin.REL_USER_INDICATOR_QUERY);
            boolean resultHasNext = result.next();
            for (int userIndex = 0; userIndex < users.size() && resultHasNext; userIndex++) {
                for (int indicatorIndex = 0; indicatorIndex < indicators.size() && resultHasNext; indicatorIndex++) {
                    if (result.getString(1).equals(users.get(userIndex)[0]) && result.getString(2).equals(indicators.get(indicatorIndex)[0])) {
                        relUserIndicator[userIndex][indicatorIndex] = true;
                        resultHasNext = result.next();
                    } else {
                        relUserIndicator[userIndex][indicatorIndex] = false;
                    }
                }  
            }
            request.setAttribute("users", users);
            request.setAttribute("indicators", indicators);
            request.setAttribute("relUserIndicator", relUserIndicator);
            request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(request, response);
        } catch (Exception ex) {
            if (ex instanceof SQLException && ((SQLException)ex).getSQLState().equals(DBUtils.SQL_STATE_INSUFFICIENT_PRIVILEGE)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                Logging.getInitializedLogger().log(Level.SEVERE, Logging.buildMessage((String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()), ex));
                ServletException servletEx = new ServletException(ex);
                servletEx.setStackTrace(ex.getStackTrace());
                throw servletEx;
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getLocalEstatusConnection();
            Statement statement = connection.createStatement();
            if (request.getParameter(Admin.ACTION_UPDATE_REL_USER_INDICATOR) != null) {
                //<editor-fold defaultstate="collapsed" desc="Assign indicators to users">
                Enumeration<String> parameters = request.getParameterNames();
                List<String[]> rows = new ArrayList<>();
                StringBuilder insertStatement = new StringBuilder(Admin.INSERT_REL_USER_INDICATOR);
                while (parameters.hasMoreElements()) {
                    String current = parameters.nextElement();
                    if (current.matches("\\d+-.+")) {
                        insertStatement.append(Admin.INSERT_REL_USER_INDICATOR_SUFFIX).append(',');
                        rows.add(current.split("-", 2));
                    }
                }
                try {
                    connection.setAutoCommit(false);
                    statement.executeUpdate(Admin.DELETE_REL_USER_INDICATOR);
                    if (!rows.isEmpty()) {
                        insertStatement.deleteCharAt(insertStatement.length() - 1);
                        insertStatement.append(';');
                        PreparedStatement preparedStatement = connection.prepareStatement(insertStatement.toString());
                        for (int i = 0; i < rows.size(); i++) {
                            preparedStatement.setInt((i * 2) + 1, Integer.parseInt(rows.get(i)[1]));
                            preparedStatement.setInt((i + 1) * 2, Integer.parseInt(rows.get(i)[0]));
                        }
                        preparedStatement.execute();
                    }
                    connection.commit();
                } catch (SQLException ex) {
                    connection.rollback();
                    throw ex;
                }
                //</editor-fold>
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (Exception ex) {
            Logging.getInitializedLogger().log(Level.SEVERE, Logging.buildMessage((String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()), ex));
            ServletException servletEx = new ServletException(ex);
            servletEx.setStackTrace(ex.getStackTrace());
            throw servletEx;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
