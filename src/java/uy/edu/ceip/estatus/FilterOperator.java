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

public enum FilterOperator {
    
    EQUAL(0), GREATER_THAN(1), LESS_THAN(2), GREATER_OR_EQUAL(3), LESS_OR_EQUAL(4), LIKE(5), IN(6);
    
    private final int code;
    
    private FilterOperator(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    public static FilterOperator fromCode(int code) throws IllegalArgumentException {
        for (FilterOperator tempFilterOperator : FilterOperator.values()) {
            if (tempFilterOperator.getCode() == code) {
                return tempFilterOperator;
            }
        }
        throw new IllegalArgumentException("No FilterOperator for given argument: " + Integer.toString(code) + ".");
    }
}
