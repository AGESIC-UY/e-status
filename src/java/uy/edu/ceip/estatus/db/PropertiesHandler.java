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
package uy.edu.ceip.estatus.db;

import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {
    
    private static final String DB_HOST = "db_host";
    private static final String DB_NAME = "db_name";
    private static final String DB_PORT = "db_port";
    private static final String ESTATUS_USER = "estatus_user";
    private static final String ESTATUS_PASSWORD = "estatus_password";
    private static final String NORMAL_USER = "normal_user";
    private static final String NORMAL_PASSWORD = "normal_user_password";
    private static final String ADMIN_USER = "admin_user";
    private static final String ADMIN_PASSWORD = "admin_user_password";
    private static final String REVISION = "revision";
    private static final String PATH = "estatus.properties";
    private static Properties properties;
    
    public static String getDBHost() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.DB_HOST);
    }
    
    public static String getDBName() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.DB_NAME);
    }
    
    public static String getDBPort() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.DB_PORT);
    }
    
    public static String getEstatusUser() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.ESTATUS_USER);
    }
    
    public static String getEstatusPassword() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.ESTATUS_PASSWORD);
    }
    
    public static String getNormalUser() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.NORMAL_USER);
    }
    
    public static String getNormalPassword() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.NORMAL_PASSWORD);
    }
    
    public static String getAdminUser() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.ADMIN_USER);
    }
    
    public static String getAdminPassword() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.ADMIN_PASSWORD);
    }
    
    public static String getRevision() throws IOException {
        return PropertiesHandler.getProperty(PropertiesHandler.REVISION);
    }
    
    private static String getProperty(String key) throws IOException {
        if (PropertiesHandler.properties == null) {
            PropertiesHandler.properties = new Properties();
            PropertiesHandler.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PropertiesHandler.PATH));
        }
        return PropertiesHandler.properties.getProperty(key);
    }
    
}
