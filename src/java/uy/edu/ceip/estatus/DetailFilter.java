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

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailFilter {
    
    private int id;
    private String name;
    private FilterType type;
    private String fieldName;
    private FilterOption[] options;
    private FilterOperator operator;
    public static SimpleDateFormat DATE_FORMAT;
    
    static {
        DetailFilter.DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    }    

    public DetailFilter(int id, String name, int type, String fieldName, FilterOption[] options, int operator) throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.type = FilterType.fromCode(type);
        this.fieldName = fieldName;
        this.options = options;
        this.operator = FilterOperator.fromCode(operator);
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public FilterType getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    public FilterOption getOption(int id) {
        boolean found = false;
        int i;
        for (i = 0; i < this.options.length && !found; i++) {
            found = this.options[i].getId() == id;
        }
        if (found) {
            return this.options[i - 1];
        } else {
            return null;
        }
    }
    
    public FilterOption[] getOptions() {
        return this.options;
    }

    public FilterOperator getOperator() {
        return operator;
    }
    
    public boolean isValid(String value) {
        try {
            switch (this.type) {
                case BOOLEAN:
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                        throw new IllegalArgumentException("Invalid boolean value: " + value);
                    }
                    break;
                case COMBO:
                    if (this.getOption(Integer.valueOf(value)) == null) {
                        throw new IllegalArgumentException("Invalid combo value: " + value);
                    }
                    break;
                case DATE:
                    DetailFilter.DATE_FORMAT.parse(value);
                    break;
                case FREE_TEXT:
                    if (value.length() == 0) {
                        throw new IllegalArgumentException("Invalid free text value: empty");
                    }
                    break;
                case NUMERIC:
                    Integer.parseInt(value);
                    break;
            }
            return true;
        } catch(IllegalArgumentException | ParseException ex) {
            return false;
        }
    }
    
    /*
    Valid combinations of FilterOperator - FilterType
    +-----------+-------+--------------+-----------+------------------+---------------+------+-----+
    |           | EQUAL | GREATER_THAN | LESS_THAN | GREATER_OR_EQUAL | LESS_OR_EQUAL | LIKE | IN  |
    +-----------+-------+--------------+-----------+------------------+---------------+------+-----+
    | BOOLEAN   | Yes   | No           | No        | No               | No            | No   | No  |
    | NUMERIC   | Yes   | Yes          | Yes       | Yes              | Yes           | No   | No  |
    | FREE_TEXT | Yes   | No           | No        | No               | No            | Yes  | No  |
    | COMBO     | No    | No           | No        | No               | No            | No   | Yes |
    | DATE      | Yes   | Yes          | Yes       | Yes              | Yes           | No   | No  |
    +-----------+-------+--------------+-----------+------------------+---------------+------+-----+
    */
    public String buildExpression(int valuesCount) {
        StringBuilder expression = new StringBuilder(this.fieldName.length() + valuesCount * 15 + 20).append(" AND "); //Estimated length
        if (this.type == FilterType.FREE_TEXT) {
            expression.append("UPPER(").append(this.fieldName).append(") ");
        } else {
            expression.append(this.fieldName).append(' ');
        }
        try {
            switch (this.operator) {
                case EQUAL:
                    switch (this.type) {
                        case BOOLEAN:
                        case NUMERIC:
                            expression.append("= ").append('?');
                            break;
                        case FREE_TEXT:
                            expression.append("= UPPER(").append('?').append(")");
                            break;
                        case DATE:
                            expression.append("= ").append('?');
                            break;
                        case COMBO:
                            throw new IllegalArgumentException();
                    }
                    break;
                case GREATER_THAN:
                    switch (this.type) {
                        case DATE:
                            expression.append("> ").append('?');
                            break;
                        case NUMERIC:
                            expression.append("> ").append('?');
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case LESS_THAN:
                    switch (this.type) {
                        case DATE:
                            expression.append("< ").append('?');
                            break;
                        case NUMERIC:
                            expression.append("< ").append('?');
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case GREATER_OR_EQUAL:
                    switch (this.type) {
                        case DATE:
                            expression.append(">= ").append('?');
                            break;
                        case NUMERIC:
                            expression.append(">= ").append('?');
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case LESS_OR_EQUAL:
                    switch (this.type) {
                        case DATE:
                            expression.append("<= ").append('?');
                            break;
                        case NUMERIC:
                            expression.append("<= ").append('?');
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case LIKE:
                    switch (this.type) {
                        case FREE_TEXT:
                            expression.append("LIKE UPPER('%' || ").append('?').append(" || '%')");
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case IN:
                    switch (this.type) {
                        case COMBO:
                            expression.append("IN (");
                            for (int i = 0; i < valuesCount; i++) {
                                expression.append('?');
                                if (i < valuesCount - 1) {
                                    expression.append(", ");
                                }
                            }
                            expression.append(")");
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
            }
            return expression.append(' ').toString();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(this.operator.name() + " operator cannot be used with data of type " + this.type.name());
        }
    }
    
    /*public static void main(String[] arg) {
        DetailFilter f;
        for (FilterType ft : FilterType.values()) {
            for (FilterOperator fo : FilterOperator.values()) {
                f = new DetailFilter("A", ft.getCode(), "field", new String[]{"option1", "option2", "option3"}, fo.getCode());
                System.out.println(ft.name() + " - " + fo.name());
                String value = "123";
                System.out.println("    value: " + value);
                System.out.println("    is valid: " + String.valueOf(f.isValid(value)));
                try {
                    System.out.println("    expression: \"" + f.buildExpression(value) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
                value = "asd";
                System.out.println("    value: " + value);
                System.out.println("    is valid: " + String.valueOf(f.isValid(value)));
                try {
                    System.out.println("    expression: \"" + f.buildExpression(value) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
                value = "true";
                System.out.println("    value: " + value);
                System.out.println("    is valid: " + String.valueOf(f.isValid(value)));
                try {
                    System.out.println("    expression: \"" + f.buildExpression(value) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
                value = "2014-05-01";
                System.out.println("    value: " + value);
                System.out.println("    is valid: " + String.valueOf(f.isValid(value)));
                try {
                    System.out.println("    expression: \"" + f.buildExpression(value) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
                String[] values = {"option1", "invalid"};
                System.out.println("    value: " + Arrays.toString(values));
                for (String option : values) {
                    System.out.println("    " + option + " is valid: " + String.valueOf(f.isValid(option)));
                }
                try {
                    System.out.println("    expression: \"" + f.buildExpression(values) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
                values[1] = "option2";
                System.out.println("    value: " + Arrays.toString(values));
                for (String option : values) {
                    System.out.println("    " + option + " is valid: " + String.valueOf(f.isValid(option)));
                }
                try {
                    System.out.println("    expression: \"" + f.buildExpression(values) + "\"");
                } catch (IllegalArgumentException ex) {
                    System.out.println("    expression: " + ex.getMessage());
                }
            }
        }
    }*/
}
