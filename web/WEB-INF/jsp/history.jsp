<%--
   Copyright 2015 Consejo de Educación Inicial y Primaria - Administración Nacional de Educación Pública - Uruguay
 
   This file is part of e-status.
 
   e-status is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
 
   e-status is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with e-status.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>e-status</title>
		<link href="//fonts.googleapis.com/css?family=Tulpen+One" rel="stylesheet" type="text/css">
		<link href="static/css/generic.css" rel="stylesheet" type="text/css">
		<link href="static/css/history.css" rel="stylesheet" type="text/css">
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
                <script src="static/highcharts_4.0.1/js/highcharts.js" type="text/javascript"></script>
                <script src="static/highcharts_4.0.1/js/modules/exporting.js" type="text/javascript"></script>
                <script src="static/highcharts_4.0.1/js/themes/grid-light.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
                <script src="static/js/history.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="background2"></div>
		<div class="panel left-panel">
			<form action="historico" method="GET">
				<h3>Evolución histórica de indicador</h3>
				<h1><c:out value="${indicatorName}"/></h1>
				<p><span style="font-weight: bold;">Descripción:</span>&nbsp;<c:out value="${indicatorExplanation}"/></p>
				<table id="filterstable"><tbody>
					<tr>
						<td><label for="groupby">Consolidar por:&nbsp;</label></td>
						<td colspan="2"><select id="groupby" class="large" name="groupby" onchange="hideGroupByInputs(); hideDayInputs();">
							<option <c:if test="${param.groupby == 2}">selected="selected"</c:if> value="2">mes</option>
							<option <c:if test="${param.groupby == 1}">selected="selected"</c:if>value="1">día</option>
							<option <c:if test="${param.groupby == 0}">selected="selected"</c:if>value="0">no consolidar</option>
						</select></td>
					</tr>
						<td><label for="groupusing">Usando el valor:&nbsp;</label></td>
						<td colspan="2"><select id="groupusing" class="large" name="groupusing">
							<option <c:if test="${param.groupusing == 'AVG'}">selected="selected"</c:if>value="AVG">promedio</option>
							<option <c:if test="${param.groupusing == 'MAX'}">selected="selected"</c:if>value="MAX">máximo</option>
							<option <c:if test="${param.groupusing == 'MIN'}">selected="selected"</c:if>value="MIN">mínimo</option>
						</select></td>
					<tr>
						<td><span>Seleccionar valores desde:&nbsp;</span></td>
						<td><select id="monthfrom" class="large" name="monthfrom" onchange="hideDays(this.id);">
							<c:forEach var="month" items="${months}" varStatus="forStatus">
								<option <c:if test="${param.monthfrom == forStatus.index + 1}">selected="selected"</c:if> value="${forStatus.index + 1}">${month}</option>
							</c:forEach>
						</select></td>
						<td><select id="dayfrom" name="dayfrom">
							<c:forEach var="day" begin="1" end="31">
								<option <c:if test="${param.dayfrom == day}">selected="selected"</c:if> value="${day}">${day}</option>
							</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td><span>Hasta:&nbsp;</span></td>
						<td><select id="monthto" class="large" name="monthto" onchange="hideDays(this.id);">
							<c:forEach var="month" items="${months}" varStatus="forStatus">
								<option
									<c:choose>
										<c:when test="${param.monthto == null && forStatus.index + 1 == currentMonth}">selected="selected"</c:when>
										<c:when test="${param.monthto == forStatus.index + 1}">selected="selected"</c:when>
									</c:choose>
									value="${forStatus.index + 1}">${month}
								</option>
							</c:forEach>
						</select></td>
						<td><select id="dayto" name="dayto">
							<c:forEach var="day" begin="1" end="31">
								<option
									<c:choose>
										<c:when test="${param.dayto == null && day == currentDay}">selected="selected"</c:when>
										<c:when test="${param.dayto == day}">selected="selected"</c:when>
									</c:choose>
									value="${day}">${day}
								</option>
							</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td><label>Año(s):&nbsp;</label></td>
						<td colspan="2"><select id="year" class="large" name="year" multiple="" required="" size="4">
							<c:forEach var="availableYear" items="${availableYears}" varStatus="forStatus">
								<option	<c:forEach var="year" items="${paramValues.year}">
										<c:if test="${year == availableYear}">selected="selected"</c:if>
									</c:forEach> 
									<c:if test="${forStatus.index == 0 && paramValues.year == null}">selected="selected"</c:if>	
									value="${availableYear}">${availableYear}</option>
							</c:forEach>
						</select></td>
					</tr>
				</tbody></table>
				<input type="hidden" name="indicator" value="${param.indicator}">
				<hr>
				<p class="center">
					<button id="web" type="submit" name="submit" value="${requestTypeWeb}">Consultar</button>
					&nbsp;&nbsp;&nbsp;
					<button id="file" type="submit" name="submit" value="${requestTypeCsv}">Consultar a archivo</button><select id="formats" onchange="changeFormat(this);" class="formats" title="Seleccione el formato del archivo">
						<option value="${requestTypeCsv}">.csv&nbsp; (Texto plano)</option>
						<option value="${requestTypeOds}">.ods&nbsp; (OpenOffice/LibreOffice)</option>
						<option value="${requestTypeXlsx}">.xlsx (Microsoft Excel)</option>
					</select>
				</p>
			</form>
			<c:if test="${table != null}">
				<hr>
				<table>
					<thead>
						<tr>
							<th colspan="3">Opciones de la gráfica</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><label for="max">Máximo del eje Y:</label></td>
							<td><input id="max" type="text"></td>
							<td rowspan="2"><input type="button" value="Volver a graficar" onclick="updateChart();"></td>
						</tr>
						<tr>
							<td><label for="min">Mínimo del eje Y:</label></td>
							<td><input id="min" type="text"></td>
						</tr>
					</tbody>
				</table>
			</c:if>
            </div>
            <div id="results">
                <c:if test="${table != null}">
			<div id="table-container-container">
				<div id="table-container">
					<table id="datatable">
						<thead>
							<tr>
								<c:forEach var="columnName" items="${tableHeader}">
								<th><div class="fixed-header">${columnName}</div></th>
								</c:forEach>
							</tr>
							<tr class="magic-header">
								<c:forEach var="columnName" items="${tableHeader}">
								<th><div>${columnName}</div></th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="row" items="${table}">
								<tr>
									<td><span class="visible">${row.formattedPeriod}</span><span class="hidden">${row.period}</span></td>
									<c:forEach var="cell" items="${row.values}" varStatus="forStatus">
									<td><span class="visible">${row.formattedValues[forStatus.index]}</span><span class="hidden">${cell}</span></td>
									</c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div id="chart"></div>
                </c:if>
            </div>
            <jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	    <input id="val_is_percentage" type="hidden" value="${val_is_percentage}">
	</body>
</html>
