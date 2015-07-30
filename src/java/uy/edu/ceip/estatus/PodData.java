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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PodData extends Data {
    
    private int id;
    private String name;
    private Date taken;
    private int tendency;
    private int classification;
    private String explanation;
    private boolean showDetail;

    public PodData(int id, String name, Date taken, Float val, boolean val_is_integer, boolean val_is_percentage, int tendency, int classification, String explanation, boolean showDetail) {
        super(new Float[] {val}, val_is_integer, val_is_percentage);
        this.id = id;
        this.name = name;
        this.taken = taken;
        this.tendency = tendency;
        this.classification = classification;
        this.explanation = explanation;
        this.showDetail = showDetail;
    }
    
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getTaken() {
        return this.taken;
    }

    public int getTendency() {
        return tendency;
    }

    public int getClassification() {
        return classification;
    }

    public String getExplanation() {
        return explanation;
    }

    public float getVal() {
        return super.getValues()[0];
    }
    
    public String getFormattedVal() {
        return super.getFormattedValues()[0];
    }
    
    public String getFormattedTaken() {
        if (this.dateFormat == null) {
            TimeZone ts = TimeZone.getTimeZone("UTC");
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
            this.dateFormat.setTimeZone(ts);
        }
        return this.dateFormat.format(taken);
    }
    
    public boolean getShowDetail() {
        return this.showDetail;
    }
}
