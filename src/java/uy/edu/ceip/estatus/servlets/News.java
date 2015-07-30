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
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class News extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            String continueURI;
            if (request.getParameter("originalURI") != null) {
                continueURI = URLDecoder.decode(request.getParameter("originalURI"), "UTF-8");
            } else {
                continueURI = request.getContextPath();
            }
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            Statement localStatement = connection.createStatement();
            ResultSet localResult = localStatement.executeQuery("SELECT val FROM parameter WHERE name = 'news_content';");
            localResult.next();
            String newsContent = localResult.getString("val");
            request.setAttribute("newsContent", newsContent);
            request.setAttribute("continueURI", continueURI);
            localResult.close();
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
        request.getRequestDispatcher("/WEB-INF/jsp/news.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response); //When the request is forwarded from Login, it is a POST request.
    }
}
