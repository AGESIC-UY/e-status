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
package uy.edu.ceip.estatus.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import uy.edu.ceip.estatus.HistoryData;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class History extends HttpServlet {
    
    private static final String QUERY_SELECT_PREFIX = "SELECT period AS \"Período\"";
    private static final String QUERY_YEAR_FIELD = ", %1$s(CASE WHEN EXTRACT('year' FROM taken) = %2$s THEN val END) AS \"%2$s\"";
    private static final String QUERY_FROM_DAY = " FROM (SELECT '2012-%1$s-%2$s'::DATE + i AS period FROM GENERATE_SERIES(0, '2012-%3$s-%4$s'::DATE - '2012-%1$s-%2$s'::DATE) AS i) AS serie JOIN \"data\" ON EXTRACT('day' FROM period) = EXTRACT('day' FROM taken) AND EXTRACT('month' FROM period) = EXTRACT('month' FROM taken)";
    private static final String QUERY_FROM_MONTH = " FROM (SELECT GENERATE_SERIES(%1$s, %2$s) AS period) AS serie JOIN \"data\" ON period = EXTRACT('month' FROM taken)";
    private static final String QUERY_SUFFIX = " JOIN indicator ON \"data\".indicator = indicator.id JOIN rel_user_indicator ON indicator.id = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE indicator.id = ? GROUP BY period ORDER BY period";
    
    public static final int GROUP_BY_NONE = 0;
    public static final int GROUP_BY_DAY = 1;
    public static final int GROUP_BY_MONTH = 2;
    
    private static final String GROUP_USING_AVG = "AVG";
    private static final String GROUP_USING_MIN = "MIN";
    private static final String GROUP_USING_MAX = "MAX";
    
    private static final String DISSAGREGATED_QUERY = "SELECT taken AS \"Período\", val AS \"%2$s\" FROM \"data\" JOIN rel_user_indicator ON \"data\".indicator = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE \"data\".indicator = ? AND DATE_TRUNC('day', taken) BETWEEN '%2$s-%3$s-%4$s' AND '%2$s-%5$s-%6$s' ORDER BY taken";
    
    public static final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"};
    
    private static final String CHECK_INDICATOR_QUERY = "SELECT name, val_is_integer, val_is_percentage, explanation FROM indicator JOIN rel_user_indicator ON indicator.id = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE indicator.id = ?";
    private static final String GET_AVAILABLE_YEARS_QUERY = "SELECT DISTINCT EXTRACT('year' FROM taken) AS taken FROM \"data\" JOIN rel_user_indicator ON \"data\".indicator = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE \"data\".indicator = ? ORDER BY taken DESC;";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int indicator, groupby, monthfrom, monthto, dayfrom, dayto;
        String groupusing, indicatorName, indicatorExplanation;
        boolean val_is_integer, val_is_percentage;
        int[] years;
        List<Integer> availableYears = new ArrayList<>();
        Map<String, String[]> parameters = request.getParameterMap();
        Connection connection = null;
        try {
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            PreparedStatement preparedStatement;
            ResultSet result;
            try {
                if (parameters.get("indicator").length != 1) {
                    throw new IllegalArgumentException("More than one indicator parameter");
                } else {
                    indicator = Integer.parseInt(parameters.get("indicator")[0]);
                    preparedStatement = connection.prepareStatement(History.CHECK_INDICATOR_QUERY);
                    preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
                    preparedStatement.setInt(2, indicator);
                    result = preparedStatement.executeQuery();
                    if (result.next()) {//Indicator exists
                        indicatorName = result.getString("name");
                        indicatorExplanation = result.getString("explanation");
                        val_is_integer = result.getBoolean("val_is_integer");
                        val_is_percentage = result.getBoolean("val_is_percentage");
                        preparedStatement = connection.prepareStatement(History.GET_AVAILABLE_YEARS_QUERY);
                        preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
                        preparedStatement.setInt(2, indicator);
                        result = preparedStatement.executeQuery();
                        while (result.next()) {
                            availableYears.add(result.getInt(1));
                        }
                        request.setAttribute("availableYears", availableYears);
                        request.setAttribute("months", History.MONTHS);
                        request.setAttribute("indicatorName", indicatorName);
                        request.setAttribute("indicatorExplanation", indicatorExplanation);
                        request.setAttribute("val_is_percentage", val_is_percentage);
                        request.setAttribute("requestTypeWeb", Detail.REQUEST_TYPE_WEB);
                        request.setAttribute("requestTypeCsv", Detail.REQUEST_TYPE_CSV);
                        request.setAttribute("requestTypeOds", Detail.REQUEST_TYPE_ODS);
                        request.setAttribute("requestTypeXlsx", Detail.REQUEST_TYPE_XLSX);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
                if (parameters.size() == 1) {
                    Calendar c = new GregorianCalendar(TimeZone.getTimeZone("America/Montevideo"));
                    request.setAttribute("currentMonth", c.get(Calendar.MONTH) + 1);
                    request.setAttribute("currentDay", c.get(Calendar.DAY_OF_MONTH));
                    request.getRequestDispatcher("/WEB-INF/jsp/history.jsp").forward(request, response);
                    return;
                } else {
                    //<editor-fold defaultstate="collapsed" desc="Parameters validation">
                    if (parameters.get("groupby").length != 1 || parameters.get("groupusing").length != 1 || parameters.get("monthfrom").length != 1 || parameters.get("monthto").length != 1 || parameters.get("dayfrom").length != 1 || parameters.get("dayto").length != 1 || parameters.get("year").length < 1) {
                        throw new IllegalArgumentException("Some parameters are missing or repeated");
                    } else {
                        groupby = Integer.parseInt(parameters.get("groupby")[0]);
                        groupusing = parameters.get("groupusing")[0];
                        monthfrom = Integer.parseInt(parameters.get("monthfrom")[0]);
                        monthto = Integer.parseInt(parameters.get("monthto")[0]);
                        dayfrom = Integer.parseInt(parameters.get("dayfrom")[0]);
                        dayto = Integer.parseInt(parameters.get("dayto")[0]);
                        years = new int[parameters.get("year").length];
                        for (int i = 0; i < years.length; i++) {
                            years[i] = Integer.parseInt(parameters.get("year")[i]);
                        }
                        if (groupby != History.GROUP_BY_NONE && groupby != History.GROUP_BY_DAY && groupby != History.GROUP_BY_MONTH) {
                            throw new IllegalArgumentException("Invalid groupby value");
                        }
                        if (!groupusing.equals(History.GROUP_USING_AVG) && !groupusing.equals(History.GROUP_USING_MAX) && !groupusing.equals(History.GROUP_USING_MIN)) {
                            throw new IllegalArgumentException("Invalid groupusing value");
                        }
                        if ((groupby == History.GROUP_BY_NONE && years.length != 1) || (groupby == History.GROUP_BY_DAY && groupby == History.GROUP_BY_MONTH && years.length < 1)) {
                            throw new IllegalArgumentException("Invalid quantity of years");
                        }
                        Calendar testCalendar = Calendar.getInstance();
                        testCalendar.setLenient(false);
                        testCalendar.clear();
                        for (int year : years) {
                            //This will throw IllegalArgumentException if any of the fields is invalid
                            testCalendar.set(year, monthfrom - 1, dayfrom);
                            Date from = testCalendar.getTime();
                            testCalendar.set(year, monthto - 1, dayto);
                            Date to = testCalendar.getTime();
                            if (from.after(to)) {
                                throw new IllegalArgumentException("First date is more recent than second date");
                            }
                        }
                    }
                    //</editor-fold>
                }
            } catch (IllegalArgumentException | NullPointerException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }    
            //<editor-fold defaultstate="collapsed" desc="Build query">
            String query = null;
            if (groupby == History.GROUP_BY_NONE) {
                query = String.format(History.DISSAGREGATED_QUERY, String.valueOf(indicator), String.valueOf(years[0]), String.valueOf(monthfrom), String.valueOf(dayfrom), String.valueOf(monthto), String.valueOf(dayto));
            } else if (groupby == History.GROUP_BY_DAY || groupby == History.GROUP_BY_MONTH) {
                StringBuilder queryBuilder = new StringBuilder(600);
                queryBuilder.append(History.QUERY_SELECT_PREFIX);
                for (int y : years) {
                    queryBuilder.append(String.format(History.QUERY_YEAR_FIELD, groupusing, String.valueOf(y)));
                }
                switch (groupby) {
                    case History.GROUP_BY_DAY:
                        queryBuilder.append(String.format(History.QUERY_FROM_DAY, String.valueOf(monthfrom), String.valueOf(dayfrom), String.valueOf(monthto), String.valueOf(dayto)));
                        break;
                    case History.GROUP_BY_MONTH:
                        queryBuilder.append(String.format(History.QUERY_FROM_MONTH, String.valueOf(monthfrom), String.valueOf(monthto)));
                        break;
                }
                queryBuilder.append(String.format(History.QUERY_SUFFIX, String.valueOf(indicator)));
                query = queryBuilder.toString();
            }
            //</editor-fold>
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
            preparedStatement.setInt(2, indicator);
            result = preparedStatement.executeQuery();
            int columnCount = result.getMetaData().getColumnCount();
            //If groupusing is AVG (and the user is grouping), the values should be formatted as float despite val_is_integer
            if (groupusing.equals(History.GROUP_USING_AVG) && groupby != History.GROUP_BY_NONE) {
                val_is_integer = false;
            }
            switch (request.getParameter("submit")) {
                case Detail.REQUEST_TYPE_WEB:
                    //<editor-fold defaultstate="collapsed" desc="Web request">
                    List<HistoryData> table = new ArrayList<>();
                    String[] tableHeader = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        tableHeader[i - 1] = result.getMetaData().getColumnLabel(i);
                    }   while (result.next()) {
                        Float[] values = new Float[columnCount - 1];
                        for (int i = 2; i <= columnCount; i++) {
                            values[i - 2] = result.getFloat(i);
                            if (result.wasNull()) {
                                values[i - 2] = null;
                            }
                        }
                        HistoryData row = new HistoryData(result.getString(1), groupby, values, val_is_integer, val_is_percentage);
                        table.add(row);
                    }   request.setAttribute("tableHeader", tableHeader);
                    request.setAttribute("table", table);
                    request.getRequestDispatcher("/WEB-INF/jsp/history.jsp").forward(request, response);
                //</editor-fold>
                    break;
                case Detail.REQUEST_TYPE_CSV:
                    //<editor-fold defaultstate="collapsed" desc="CSV request">
                    response.setContentType("text/csv;charset=UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=\"histórico.csv\"");
                    PrintWriter writer = response.getWriter();
                    for (int i = 1; i <= columnCount; i++) {
                        writer.append(Detail.CSV_QUOTE).append(result.getMetaData().getColumnLabel(i).replace(Detail.CSV_QUOTE, Detail.CSV_QUOTE + Detail.CSV_QUOTE)).append(Detail.CSV_QUOTE);
                        if (i == columnCount) {
                            writer.println();
                        } else {
                            writer.append(Detail.CSV_DELIMITER);
                        }
                    }   while (result.next()) {
                        Float[] values = new Float[columnCount - 1];
                        for (int i = 2; i <= columnCount; i++) {
                            values[i - 2] = result.getFloat(i);
                            if (result.wasNull()) {
                                values[i - 2] = null;
                            }
                        }
                        HistoryData row = new HistoryData(result.getString(1), groupby, values, val_is_integer, val_is_percentage);
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                writer.append(Detail.CSV_QUOTE).append(row.getFormattedPeriod().replace(Detail.CSV_QUOTE, Detail.CSV_QUOTE + Detail.CSV_QUOTE)).append(Detail.CSV_QUOTE);
                            } else {
                                writer.append(Detail.CSV_QUOTE).append(row.getFormattedValues()[i - 2].replace(Detail.CSV_QUOTE, Detail.CSV_QUOTE + Detail.CSV_QUOTE)).append(Detail.CSV_QUOTE);
                            }
                            if (i == columnCount) {
                                writer.println();
                            } else {
                                writer.append(Detail.CSV_DELIMITER);
                            }
                        }
                    }
                    //</editor-fold>
                    break;
                case Detail.REQUEST_TYPE_ODS:
                    //<editor-fold defaultstate="collapsed" desc="ODS request">
                    response.setContentType("application/vnd.oasis.opendocument.spreadsheet;charset=UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=\"histórico.ods\"");
                    ArrayList<Object[]> resultList = new ArrayList<>();
                    String[] header = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        header[i - 1] = result.getMetaData().getColumnLabel(i);
                    }   while (result.next()) {
                        Float[] values = new Float[columnCount - 1];
                        for (int i = 2; i <= columnCount; i++) {
                            values[i - 2] = result.getFloat(i);
                            if (result.wasNull()) {
                                values[i - 2] = null;
                            }
                        }
                        HistoryData hd = new HistoryData(result.getString(1), groupby, values, val_is_integer, val_is_percentage);
                        Object[] row = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                row[i - 1] = hd.getFormattedPeriod();
                            } else if (hd.getValues()[i - 2] != null) {
                                if (val_is_percentage) {
                                    row[i - 1] = hd.getValues()[i - 2].doubleValue() / 100;
                                } else {
                                    row[i - 1] = hd.getValues()[i - 2].doubleValue();
                                }
                            }
                            
                        }
                        resultList.add(row);
                    }   TableModel model = new DefaultTableModel(resultList.toArray(new Object[0][0]), header);
                    SpreadSheet.createEmpty(model).getPackage().save(response.getOutputStream());
                    //</editor-fold>
                    break;
                case Detail.REQUEST_TYPE_XLSX:
                    //<editor-fold defaultstate="collapsed" desc="XLSX request">
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=\"histórico.xlsx\"");
                    XSSFWorkbook xlsx = new XSSFWorkbook();
                    XSSFSheet sheet = xlsx.createSheet();
                    org.apache.poi.ss.usermodel.Cell cell;
                    Row sheetRow = sheet.createRow(0);
                    for (int i = 1; i <= columnCount; i++) {
                        cell = sheetRow.createCell(i - 1);
                        cell.setCellValue(result.getMetaData().getColumnLabel(i));
                    }   int rowIndex = 1;
                    while (result.next()) {
                        sheetRow = sheet.createRow(rowIndex);
                        Float[] values = new Float[columnCount - 1];
                        for (int i = 2; i <= columnCount; i++) {
                            values[i - 2] = result.getFloat(i);
                            if (result.wasNull()) {
                                values[i - 2] = null;
                            }
                        }
                        HistoryData row = new HistoryData(result.getString(1), groupby, values, val_is_integer, val_is_percentage);
                        for (int i = 1; i <= columnCount; i++) {
                            cell = sheetRow.createCell(i - 1);
                            if (i == 1) {
                                cell.setCellValue(row.getFormattedPeriod());
                            } else if (row.getValues()[i - 2] != null) {
                                
                                if (val_is_percentage) {
                                    cell.setCellValue(row.getValues()[i - 2].doubleValue() / 100);
                            } else {
                                    cell.setCellValue(row.getValues()[i - 2].doubleValue());
                            }
                        }
                    }
                    rowIndex++;
                }   xlsx.write(response.getOutputStream());
                //</editor-fold>
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        } catch (Exception ex) {
            Logging.getInitializedLogger().log(Level.SEVERE, Logging.buildMessage((String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY), request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()), ex));
            ServletException servletEx = new ServletException(ex);
            servletEx.setStackTrace(ex.getStackTrace());
            throw servletEx;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

}
