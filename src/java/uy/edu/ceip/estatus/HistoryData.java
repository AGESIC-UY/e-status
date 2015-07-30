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

import uy.edu.ceip.estatus.servlets.History;

public class HistoryData extends Data {
    
    private String period;
    private int groupby;

    public HistoryData(String period, int groupby, Float[] values, boolean val_is_integer, boolean val_is_percentage) {
        super(values, val_is_integer, val_is_percentage);
        this.period = period;
        this.groupby = groupby;
    }

    public String getPeriod() {
        return period;
    }
    
    public String getFormattedPeriod() {
        String result;
        switch (groupby) {
            case History.GROUP_BY_DAY:
                result = period.substring(8, 10) + "/" + period.substring(5, 7);
                break;
            case History.GROUP_BY_MONTH:
                result = History.MONTHS[Integer.parseInt(period) - 1];
                break;
            case History.GROUP_BY_NONE:
                result = period.substring(8, 10) + "/" + period.substring(5, 7) + period.substring(10, 19);
                break;
            default:
                throw new IllegalArgumentException("Invalid groupby value");
        }
        return result;
    }
    
}
