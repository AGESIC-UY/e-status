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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    
    private static final String ADMIN_ROLE_NAME = "admin";
    private static final String USER_ROLE_NAME = "user";
    
    /**
     * Returns a connection to the local database logged in as the "estatus" superuser.
     */
    public static Connection getLocalEstatusConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        String host = PropertiesHandler.getDBHost();
        String port = PropertiesHandler.getDBPort();
        String name = PropertiesHandler.getDBName();
        String user = PropertiesHandler.getEstatusUser();
        String password = PropertiesHandler.getEstatusPassword();
        return ConnectionFactory.getConnection(user, password, host, port, name);
    }
    
    /**
     * Returns a connection to the local database logged as the appropiate user based on the role.
     */
    public static Connection getLocalConnection(String role) throws IOException, SQLException, ClassNotFoundException {
        String host = PropertiesHandler.getDBHost();
        String port = PropertiesHandler.getDBPort();
        String name = PropertiesHandler.getDBName();
        String user;
        String password;
        switch (role) {
            case ConnectionFactory.ADMIN_ROLE_NAME:
                user = PropertiesHandler.getAdminUser();
                password = PropertiesHandler.getAdminPassword();
                return ConnectionFactory.getConnection(user, password, host, port, name);
            case ConnectionFactory.USER_ROLE_NAME:
                user = PropertiesHandler.getNormalUser();
                password = PropertiesHandler.getNormalPassword();
                return ConnectionFactory.getConnection(user, password, host, port, name);
            default:
                return null;
        }
    }
    
    /**
     * Returns a connection to the GURI database.
     */
    public static Connection getGURIConnection() throws SQLException, ClassNotFoundException, IOException {
        Connection localConnection = null;
        try {
            localConnection = ConnectionFactory.getLocalEstatusConnection();
            Statement localStatement = localConnection.createStatement();
            ResultSet localResult;
            localResult = localStatement.executeQuery("SELECT (SELECT val FROM parameter WHERE name = 'host') AS host, (SELECT val FROM parameter WHERE name = 'name') AS name, (SELECT val FROM parameter WHERE name = 'port') AS port, (SELECT val FROM parameter WHERE name = 'user') AS \"user\", (SELECT val FROM parameter WHERE name = 'password') AS \"password\";");
            localResult.next();
            String host = localResult.getString("host");
            String port = localResult.getString("port");
            String name = localResult.getString("name");
            String user = localResult.getString("user");
            String password = localResult.getString("password");
            return ConnectionFactory.getConnection(user, password, host, port, name);
        } finally {
            if (localConnection != null) {
                localConnection.close();
            }
        }
    }
    
    private static Connection getConnection(String user, String password, String host, String port, String name) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + name, user, password);
        return connection;
    }
}
