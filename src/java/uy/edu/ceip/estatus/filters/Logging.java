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
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import uy.edu.ceip.JDBCHandler;
import uy.edu.ceip.estatus.db.ConnectionFactory;

/**
 * Logs access to all servlets
 */
public class Logging implements Filter {
    
    public static final String LOG_TABLE_NAME = "log";
    
    private Logger logger;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.logger = Logging.getInitializedLogger();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest)request).getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY) != null) {
            this.logger.log(Level.INFO, Logging.buildMessage((String)((HttpServletRequest)request).getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), ((HttpServletRequest)request).getRequestURI() + (((HttpServletRequest)request).getQueryString() == null ? "" : "?" + ((HttpServletRequest)request).getQueryString())));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }
    
    public static Logger getInitializedLogger() {
        Logger logger = Logger.getLogger("uy.edu.ceip.estatus");
        boolean found = false;
        int i = 0;
        Handler[] handlers = logger.getHandlers();
        while (!found && i < handlers.length) {
            found = handlers[i] instanceof JDBCHandler;
            i++;
        }
        if (!found) {
            try {
                logger.addHandler(new JDBCHandler(ConnectionFactory.getLocalEstatusConnection(), Logging.LOG_TABLE_NAME));
                logger.setLevel(Level.ALL);
                logger.setUseParentHandlers(false);
            } catch (Exception ex) {
                Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logger;
    }
    
    public static String buildMessage(String user, String URL) {
        return Logging.buildMessage(user, URL, null);
    }
    
    public static String buildMessage(String user, String url, Exception ex) {
        StringBuilder message = new StringBuilder(user.length() + url.length());
        message.append(user);
        message.append('@');
        message.append(url);
        if (ex != null) {
            message.append('\n');
            message.append(ex.getMessage());
            for (StackTraceElement ste : ex.getStackTrace()) {
                message.append('\n');
                message.append(ste.toString());
            }
        }
        return message.toString();
    }
    
}
