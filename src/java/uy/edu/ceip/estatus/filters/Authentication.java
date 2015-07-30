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
package uy.edu.ceip.estatus.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.servlets.Detail;

public class Authentication implements Filter {
    
    public static final String USER_ATTRIBUTE_KEY = "user";
    public static final String ROLE_ATTRIBUTE_KEY = "role";
    public static final String MUST_RESET_PASSWORD_ATTRIBUTE_KEY = "reset";
    
    private static final String LOCAL_USER_QUERY = "SELECT local_role_name, must_see_news FROM local_user JOIN local_role ON local_user.local_role = local_role.id WHERE local_user_name = ?";
    private static final String UPDATE_USER_NEWS = "UPDATE local_user SET must_see_news = false WHERE local_user_name = ?";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest castedRequest = (HttpServletRequest)request;
        HttpServletResponse castedResponse = (HttpServletResponse)response;
        if (castedRequest.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY) == null) {
            if (castedRequest.getRequestURI().equals(castedRequest.getContextPath() + "/recargar")) {
                castedResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            } else {
                String originalURI = Util.URLEncode(castedRequest.getRequestURI() + (castedRequest.getQueryString() == null ? "" : "?" + castedRequest.getQueryString()), "UTF-8");
                Assertion casAssertion = (Assertion)castedRequest.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
                Connection connection = null;
                try {
                    connection = ConnectionFactory.getLocalEstatusConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(Authentication.LOCAL_USER_QUERY);
                    preparedStatement.setString(1, casAssertion.getPrincipal().getName());
                    ResultSet result = preparedStatement.executeQuery();
                    try {
                        result.next();
                        castedRequest.getSession().setAttribute(Authentication.ROLE_ATTRIBUTE_KEY, result.getString("local_role_name"));
                        castedRequest.getSession().setAttribute(Authentication.USER_ATTRIBUTE_KEY, casAssertion.getPrincipal().getName());
                        //Check if the user is to be redirected to the news page
                        boolean mustSeeNews = result.getBoolean("must_see_news");
                        preparedStatement.close();
                        if (mustSeeNews) {
                            preparedStatement = connection.prepareStatement(Authentication.UPDATE_USER_NEWS);
                            preparedStatement.setString(1, casAssertion.getPrincipal().getName());
                            preparedStatement.executeUpdate();
                            castedResponse.sendRedirect(castedRequest.getContextPath() + "/noticias?originalURI=" + originalURI);
                        } else {
                            chain.doFilter(request, response);
                        }
                    } catch (SQLException ex) {//No result
                        castedResponse.sendError(HttpServletResponse.SC_FORBIDDEN);//Debería mostrar un mensaje más claro
                    }
                    result.close();
                } catch (Exception ex) {
                    Logging.getInitializedLogger().log(Level.SEVERE, Logging.buildMessage((String)castedRequest.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), castedRequest.getRequestURI() + (castedRequest.getQueryString() == null ? "" : "?" + castedRequest.getQueryString()), ex));
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
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        
    }
    
}
