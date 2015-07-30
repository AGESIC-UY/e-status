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

import java.util.Date;

public class Indicator {

    private int id;
    private String name;
    private String query;
    private Date lastUpdated;
    private int updateInterval;

    public Indicator(int id, String name, String query, Date lastUpdated, int updateInterval) {
        this.id = id;
        this.name = name;
        this.query = query;
        this.lastUpdated = lastUpdated;
        this.updateInterval = updateInterval;
    }

    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public String getQuery() {
        return this.query;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }

    public boolean mustUpdate(Date currentDate) {
        return this.lastUpdated == null || (Math.ceil((currentDate.getTime() - this.lastUpdated.getTime()) / 1000 / 60) >= this.updateInterval);
    }
}
