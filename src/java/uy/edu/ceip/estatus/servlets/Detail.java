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
import java.util.List;
import java.util.Map;
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
import uy.edu.ceip.estatus.DetailFilter;
import uy.edu.ceip.estatus.FilterOption;
import uy.edu.ceip.estatus.FilterType;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class Detail extends HttpServlet {
    
    private static final String CHECK_INDICATOR_QUERY = "SELECT name, explanation, detail FROM indicator JOIN rel_user_indicator ON indicator.id = rel_user_indicator.indicator JOIN local_user ON rel_user_indicator.localuser = local_user.id AND local_user.local_user_name = ? WHERE indicator.id = ?";
    private static final String FILTERS_QUERY = "SELECT filter.* FROM filter JOIN rel_filter_indicator ON filter.id = rel_filter_indicator.filter WHERE indicator = ? ORDER BY filter_order;";
    private static final String FILTERS_OPTIONS_QUERY = "SELECT filter_option.filter, filter_option.id, option_value FROM filter_option JOIN filter ON filter_option.filter = filter.id JOIN rel_filter_indicator ON filter.id = rel_filter_indicator.filter WHERE indicator = ? ORDER BY option_value;";
    private static final String FILTERS_WILDCARD = "--filters";
    public static final String REQUEST_TYPE_WEB = "w";
    public static final String REQUEST_TYPE_ODS = "o";
    public static final String REQUEST_TYPE_XLSX = "x";
    public static final String REQUEST_TYPE_CSV = "c";
    public static final String CSV_DELIMITER = "	"; //Tab
    public static final String CSV_QUOTE = "\"";
    private static final String FILE_DOWNLOAD_WARNING_USED_KEY = "warned";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int indicator;
        String indicatorName, indicatorExplanation, detail;
        Map<String, String[]> parameters = request.getParameterMap();
        List<DetailFilter> detailFilters = new ArrayList<>();
        List<DetailFilter> usedDetailFilters = new ArrayList<>();
        Connection connection = null, guriConnection = null;
        try {
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            PreparedStatement preparedStatement;
            ResultSet filters;
            ResultSet options;
            try {
                if (parameters.get("indicator").length != 1) {
                    throw new IllegalArgumentException("More than one indicator parameter");
                } else {
                    indicator = Integer.parseInt(parameters.get("indicator")[0]);
                    preparedStatement = connection.prepareStatement(Detail.CHECK_INDICATOR_QUERY);
                    preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
                    preparedStatement.setInt(2, indicator);
                    filters = preparedStatement.executeQuery();
                    if (filters.next()) {//Indicator exists
                        indicatorName = filters.getString("name");
                        indicatorExplanation = filters.getString("explanation");
                        detail = filters.getString("detail");
                        preparedStatement = connection.prepareStatement(Detail.FILTERS_QUERY);
                        preparedStatement.setInt(1, indicator);
                        filters = preparedStatement.executeQuery();
                        preparedStatement = connection.prepareStatement(Detail.FILTERS_OPTIONS_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        preparedStatement.setInt(1, indicator);
                        options = preparedStatement.executeQuery();
                        while (filters.next()) {
                            List<FilterOption> filterOptions = new ArrayList<>();
                            options.absolute(0);
                            while (options.next()) {
                                if (options.getInt("filter") == filters.getInt("id")) {
                                    filterOptions.add(new FilterOption(options.getInt("id"), options.getString("option_value")));
                                }
                            }
                            detailFilters.add(new DetailFilter(filters.getInt("id"), filters.getString("name"), filters.getInt("filter_type"), filters.getString("field_name"), filterOptions.toArray(new FilterOption[0]), filters.getInt("filter_operator")));
                        }
                        request.setAttribute("filters", detailFilters);
                        request.setAttribute("indicatorName", indicatorName);
                        request.setAttribute("indicatorExplanation", indicatorExplanation);
                        request.setAttribute("requestTypeWeb", Detail.REQUEST_TYPE_WEB);
                        request.setAttribute("requestTypeCsv", Detail.REQUEST_TYPE_CSV);
                        request.setAttribute("requestTypeOds", Detail.REQUEST_TYPE_ODS);
                        request.setAttribute("requestTypeXlsx", Detail.REQUEST_TYPE_XLSX);
                        if (request.getSession().getAttribute(Detail.FILE_DOWNLOAD_WARNING_USED_KEY) != null) {
                            request.setAttribute("fileDownloadWarningUsedKey", Detail.FILE_DOWNLOAD_WARNING_USED_KEY);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
                if (parameters.get("isquery") == null) {
                    request.getRequestDispatcher("/WEB-INF/jsp/detail.jsp").forward(request, response);
                    return;
                } else {
                    //<editor-fold defaultstate="collapsed" desc="Parameters validation">
                    for (String parameter : parameters.keySet()) {
                        boolean parameterIsValid = false;
                        int i = 0;
                        if (parameter.equals("indicator") || parameter.equals("isquery") || parameter.equals("submit")) {
                            parameterIsValid = true;
                        } else {
                            while (!parameterIsValid && i < detailFilters.size()) {
                                if (String.valueOf(detailFilters.get(i).getId()).equals(parameter)) {
                                    parameterIsValid = true;
                                } else {
                                    i++;
                                }
                            }
                            if (parameterIsValid) {
                                if (detailFilters.get(i).getType() == FilterType.COMBO) {
                                    for (String option : parameters.get(parameter)) {
                                        if (!detailFilters.get(i).isValid(option)) {
                                            parameterIsValid = false;
                                            break;
                                        }
                                    }
                                } else if (detailFilters.get(i).getType() == FilterType.BOOLEAN || detailFilters.get(i).getType() == FilterType.DATE || detailFilters.get(i).getType() == FilterType.FREE_TEXT || detailFilters.get(i).getType() == FilterType.NUMERIC) {
                                    if (parameters.get(parameter).length != 1 || !detailFilters.get(i).isValid(parameters.get(parameter)[0])) {
                                        parameterIsValid = false;
                                    }
                                } else {
                                    parameterIsValid = false;
                                }
                            }
                        }
                        if (!parameterIsValid) {
                            throw new IllegalArgumentException("Duplicate or invalid filter");
                        } else {
                            if (!parameter.equals("indicator") && !parameter.equals("isquery") && !parameter.equals("submit")) {
                                usedDetailFilters.add(detailFilters.get(i));
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
            StringBuilder query = new StringBuilder(detail);
            int filtersIndex = query.indexOf(Detail.FILTERS_WILDCARD);
            if (filtersIndex == -1) {
                throw new IllegalArgumentException("Detail query doesn't have the filters wildcard");
            }
            query.delete(filtersIndex, filtersIndex + Detail.FILTERS_WILDCARD.length());
            for (DetailFilter detailFilter : usedDetailFilters) {
                String expression = detailFilter.buildExpression(parameters.get(String.valueOf(detailFilter.getId())).length);
                query.insert(filtersIndex, expression);
                filtersIndex += expression.length();
            }
            //</editor-fold>
            guriConnection = ConnectionFactory.getGURIConnection();
            PreparedStatement guriStatement = guriConnection.prepareStatement(query.toString());
            int parameterIndex = 1;
            System.out.println(query.toString());
            for (DetailFilter usedDetailFilter : usedDetailFilters) {
                for (String value : parameters.get(String.valueOf(usedDetailFilter.getId()))) {
                    switch (usedDetailFilter.getType()) {
                        case BOOLEAN:
                            guriStatement.setBoolean(parameterIndex, Boolean.parseBoolean(value));
                            break;
                        case NUMERIC:
                            guriStatement.setInt(parameterIndex, Integer.parseInt(value));
                            break;
                        case FREE_TEXT:
                            guriStatement.setString(parameterIndex, value);
                            break;
                        case DATE:
                            guriStatement.setDate(parameterIndex, new java.sql.Date(DetailFilter.DATE_FORMAT.parse(value).getTime()));
                            break;
                        case COMBO:
                            guriStatement.setString(parameterIndex, usedDetailFilter.getOption(Integer.parseInt(value)).getOption());
                            break;
                    }
                    parameterIndex++;
                }
            }
            ResultSet result;
            result = guriStatement.executeQuery();
            int columnCount = result.getMetaData().getColumnCount();
            switch (request.getParameter("submit")) {
                case Detail.REQUEST_TYPE_WEB:
                    //<editor-fold defaultstate="collapsed" desc="Web request">
                    String[] tableHeader;
                    List<String[]> table = new ArrayList<>();
                    tableHeader = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        tableHeader[i - 1] = result.getMetaData().getColumnLabel(i);
                    }   while (result.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            row[i - 1] = result.getString(i);
                        }
                        table.add(row);
                    }   request.getSession().setAttribute(Detail.FILE_DOWNLOAD_WARNING_USED_KEY, Detail.FILE_DOWNLOAD_WARNING_USED_KEY);
                    request.setAttribute("tableHeader", tableHeader);
                    request.setAttribute("table", table);
                    request.setAttribute("tableSize", table.size());
                    request.setAttribute("fileDownloadWarningUsedKey", Detail.FILE_DOWNLOAD_WARNING_USED_KEY);
                    request.getRequestDispatcher("/WEB-INF/jsp/detail.jsp").forward(request, response);
                //</editor-fold>
                    break;
                case Detail.REQUEST_TYPE_CSV:
                    //<editor-fold defaultstate="collapsed" desc="CSV request">
                    response.setContentType("text/csv;charset=UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=\"detalle.csv\"");
                    PrintWriter writer = response.getWriter();
                    for (int i = 1; i <= columnCount; i++) {
                        writer.append(Detail.CSV_QUOTE).append(result.getMetaData().getColumnLabel(i).replace(Detail.CSV_QUOTE, Detail.CSV_QUOTE + Detail.CSV_QUOTE)).append(Detail.CSV_QUOTE);
                        if (i == columnCount) {
                            writer.println();
                        } else {
                            writer.append(Detail.CSV_DELIMITER);
                        }
                    }   while (result.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String cell = result.getString(i);
                            writer.append(Detail.CSV_QUOTE).append(cell == null ? "" : cell.replace(Detail.CSV_QUOTE, Detail.CSV_QUOTE + Detail.CSV_QUOTE)).append(Detail.CSV_QUOTE);
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
                    response.setHeader("Content-Disposition", "attachment;filename=\"detalle.ods\"");
                    ArrayList<Object[]> resultList = new ArrayList<>();
                    String[] header = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        header[i - 1] = result.getMetaData().getColumnLabel(i);
                    }   while (result.next()) {
                        Object[] row = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            if (result.getMetaData().getColumnType(i) == java.sql.Types.TINYINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.SMALLINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.INTEGER
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.BIGINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.FLOAT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.REAL
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.DOUBLE
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.NUMERIC
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.DECIMAL) {
                                row[i - 1] = result.getDouble(i);
                            } else {
                                row[i - 1] = result.getString(i);
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
                    response.setHeader("Content-Disposition", "attachment;filename=\"detalle.xlsx\"");
                    XSSFWorkbook xlsx = new XSSFWorkbook();
                    XSSFSheet sheet = xlsx.createSheet();
                    org.apache.poi.ss.usermodel.Cell cell;
                    Row row = sheet.createRow(0);
                    for (int i = 1; i <= columnCount; i++) {
                        cell = row.createCell(i - 1);
                        cell.setCellValue(result.getMetaData().getColumnLabel(i));
                    }   int rowIndex = 1;
                    while (result.next()) {
                        row = sheet.createRow(rowIndex);
                        for (int i = 1; i <= columnCount; i++) {
                            cell = row.createCell(i - 1);
                            if (result.getMetaData().getColumnType(i) == java.sql.Types.TINYINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.SMALLINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.INTEGER
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.BIGINT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.FLOAT
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.REAL
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.DOUBLE
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.NUMERIC
                                    || result.getMetaData().getColumnType(i) == java.sql.Types.DECIMAL) {
                                cell.setCellValue(result.getDouble(i));
                            } else {
                            cell.setCellValue(result.getString(i));
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
            if (guriConnection != null) {
                try {
                    guriConnection.close();
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
