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

public class DBUtils {
    
    public static final String SQL_STATE_INSUFFICIENT_PRIVILEGE = "42501";
    public static final String SQL_STATE_INVALID_PASSWORD = "28P01";
    public static final String SQL_STATE_INVALID_AUTHORIZATION_SPECIFICATION = "28000";
    
    public static String encodeStringLiteral(String s) {
        return s.replaceAll("[\n\\\\]", "").replaceAll("'", "''");
    }
    
    public static String encodeObjectName(String s) {
        return s.replaceAll("[\n\\\\]", "").replaceAll("\"", "\"\"");
    }
}
