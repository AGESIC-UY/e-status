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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uy.edu.ceip.estatus.PodData;
import uy.edu.ceip.estatus.db.ConnectionFactory;
import uy.edu.ceip.estatus.filters.Authentication;
import uy.edu.ceip.estatus.filters.Logging;

public class Reload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            Document xmlResponse = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            xmlResponse.appendChild(xmlResponse.createElement("data"));
            connection = ConnectionFactory.getLocalConnection((String)request.getSession().getAttribute(Authentication.ROLE_ATTRIBUTE_KEY));
            PreparedStatement preparedStatement = connection.prepareStatement(Main.DATA_QUERY);
            preparedStatement.setString(1, (String)request.getSession().getAttribute(Authentication.USER_ATTRIBUTE_KEY));
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                PodData data = new PodData(result.getInt("id"), result.getString("name"), result.getTimestamp("taken"), result.getFloat("val"), result.getBoolean("val_is_integer"), result.getBoolean("val_is_percentage"), result.getInt("tendency"), result.getInt("classification"), result.getString("explanation"), result.getBoolean("show_detail"));
                Element indicator = xmlResponse.createElement("indicator");
                Element id = xmlResponse.createElement("id");
                Element name = xmlResponse.createElement("name");
                Element val = xmlResponse.createElement("val");
                Element taken = xmlResponse.createElement("taken");
                Element tendency = xmlResponse.createElement("tendency");
                Element classification = xmlResponse.createElement("classification");
                Element explanation = xmlResponse.createElement("explanation");
                id.appendChild(xmlResponse.createTextNode(String.valueOf(data.getId())));
                name.appendChild(xmlResponse.createTextNode(data.getName()));
                val.appendChild(xmlResponse.createTextNode(data.getFormattedVal()));
                taken.appendChild(xmlResponse.createTextNode(data.getFormattedTaken()));
                tendency.appendChild(xmlResponse.createTextNode(String.valueOf(data.getTendency())));
                classification.appendChild(xmlResponse.createTextNode(String.valueOf(data.getClassification())));
                explanation.appendChild(xmlResponse.createTextNode(String.valueOf(data.getExplanation())));
                indicator.appendChild(id);
                indicator.appendChild(name);
                indicator.appendChild(val);
                indicator.appendChild(taken);
                indicator.appendChild(tendency);
                indicator.appendChild(classification);
                indicator.appendChild(explanation);
                xmlResponse.getFirstChild().appendChild(indicator);
            }
            response.setContentType("text/xml;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            DOMSource source = new DOMSource(xmlResponse);
            StreamResult xmlResult = new StreamResult(response.getWriter());
            TransformerFactory.newInstance().newTransformer().transform(source, xmlResult);
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
