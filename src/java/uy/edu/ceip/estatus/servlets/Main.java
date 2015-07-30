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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uy.edu.ceip.estatus.PodData;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class Main extends HttpServlet {
    
    static final String DATA_QUERY = "SELECT * FROM (SELECT indicator.id, indicator.name, val, taken, classification, val_is_integer, val_is_percentage, explanation, detail IS NOT NULL AS show_detail, CASE WHEN val > (SELECT val FROM data d3 WHERE d3.indicator = d1.indicator ORDER BY d3.taken DESC OFFSET 1 LIMIT 1) THEN 1 ELSE CASE WHEN val < (SELECT val FROM data d3 WHERE d3.indicator = d1.indicator ORDER BY d3.taken DESC OFFSET 1 LIMIT 1) THEN -1 ELSE 0 END END AS tendency, RANK() OVER (PARTITION BY d1.indicator ORDER BY d1.taken DESC) FROM \"data\" d1 JOIN indicator ON d1.indicator = indicator.id JOIN rel_user_indicator ON indicator.id = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE CURRENT_TIMESTAMP - taken < update_interval * INTERVAL '10 minutes' ) AS sub WHERE rank = 1 ORDER BY classification::TEXT";
    private static final String AJAX_PARAMETER_QUERY = "SELECT val FROM parameter WHERE name = 'ajax_period'";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            PreparedStatement preparedStatement = connection.prepareStatement(Main.AJAX_PARAMETER_QUERY);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            request.setAttribute("ajax_period", result.getString("val"));
            preparedStatement = connection.prepareStatement(Main.DATA_QUERY);
            preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
            result = preparedStatement.executeQuery();
            ArrayList<PodData> indicators = new ArrayList<>();
            while (result.next()) {
                indicators.add(new PodData(result.getInt("id"), result.getString("name"), result.getTimestamp("taken"), result.getFloat("val"), result.getBoolean("val_is_integer"), result.getBoolean("val_is_percentage"), result.getInt("tendency"), result.getInt("classification"), result.getString("explanation"), result.getBoolean("show_detail")));
            }
            request.setAttribute("indicators", indicators);
            response.setContentType("text/html;charset=UTF-8");
            request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
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
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
