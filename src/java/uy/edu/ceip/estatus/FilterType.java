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
package uy.edu.ceip.estatus;

public enum FilterType {
    
    BOOLEAN(0), NUMERIC(1), FREE_TEXT(2), COMBO(3), DATE(4);
    
    private final int code;
    
    private FilterType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    public static FilterType fromCode(int code) throws IllegalArgumentException {
        for (FilterType tempFilterType : FilterType.values()) {
            if (tempFilterType.getCode() == code) {
                return tempFilterType;
            }
        }
        throw new IllegalArgumentException("No FilterType for given argument: " + Integer.toString(code) + ".");
    }
}
