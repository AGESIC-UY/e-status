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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Data {
    
    protected Float[] values;
    protected String[] formattedValues;
    protected boolean val_is_integer; 
    protected boolean val_is_percentage;
    protected SimpleDateFormat dateFormat;
    protected NumberFormat integerFormat;
    protected NumberFormat realFormat;
    protected NumberFormat percentageFormat;

    public Data(Float[] values, boolean val_is_integer, boolean val_is_percentage) {
        this.values = values;
        this.val_is_integer = val_is_integer;
        this.val_is_percentage = val_is_percentage;
        this.formattedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.formattedValues[i] = this.getFormattedVal(this.values[i]);
            if (this.val_is_percentage && this.values[i] != null) {
                this.values[i] = this.values[i] * 100;
            }
        }
    }

    public Float[] getValues() {
        return values;
    }

    public String[] getFormattedValues() {
        return formattedValues;
    }

    public boolean isVal_is_integer() {
        return val_is_integer;
    }

    public boolean isVal_is_percentage() {
        return val_is_percentage;
    }
    
    private String getFormattedVal(Float number) {
        if (this.integerFormat == null) {
        this.integerFormat = NumberFormat.getNumberInstance(new Locale("es", "UY"));
        this.realFormat = NumberFormat.getNumberInstance(new Locale("es", "UY"));
        this.percentageFormat = NumberFormat.getPercentInstance(new Locale("es", "UY"));
        this.integerFormat.setMaximumFractionDigits(0);
        this.realFormat.setMinimumFractionDigits(2);
        this.realFormat.setMaximumFractionDigits(2);
        this.percentageFormat.setMinimumFractionDigits(2);
        this.percentageFormat.setMaximumFractionDigits(2);
        }
        if (number == null) {
            return "";
        } else {
            if (this.val_is_integer) {
                return this.integerFormat.format(number);
            } else if (this.val_is_percentage) {
                return this.percentageFormat.format(number);
            } else {
                return this.realFormat.format(number);
            }
        }
    }
}
