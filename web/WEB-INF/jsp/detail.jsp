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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>e-status</title>
		<link href="//fonts.googleapis.com/css?family=Tulpen+One" rel="stylesheet" type="text/css">
		<link href="static/css/generic.css" rel="stylesheet" type="text/css">
		<link href="static/css/detail.css" rel="stylesheet" type="text/css">
                <link href="static/css/jquery-ui/flick/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css">
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
		<script src="static/jquery/jquery.timeago.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
		<script src="static/js/detail.js" type="text/javascript"></script>
		<script src="static/spin/spin.min.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="background2"></div>
		<div id="filters" class="panel left-panel">
			<form action="detalle" method="GET">
				<h3>Detalle de indicador</h3>
				<h1><c:out value="${indicatorName}"/></h1>
				<p><span style="font-weight: bold;">Descripción:</span>&nbsp;<c:out value="${indicatorExplanation}"/></p>
				<div class="table-row">
					<div class="position">
						<div class="scroll">
							<table id="filterstable">
								<tbody>
									<c:forEach var="filter" items="${filters}">
										<tr>
											<c:set var="key">${filter.id}</c:set>
											<td><input id="fh${filter.id}" class="fh" type="checkbox" <c:if test="${param[key] != null}">checked="checked"</c:if>></td>
											<td><label for="fh${filter.id}"><c:out value="${filter.name}"/>:&nbsp;</label></td>
											<td>
												<c:choose>
													<c:when test="${filter.type == 'BOOLEAN'}">
														<label for="${filter.id}_1">Sí</label><input id="${filter.id}_1" name="${filter.id}" class="filter" type="radio" value="true" <c:choose><c:when test="${param[key] == null}">disabled="disabled" checked="checked"</c:when><c:when test="${param[key] == 'true'}">checked="checked"</c:when></c:choose>>
														&nbsp;
														<label for="${filter.id}_2">No</label><input id="${filter.id}_2" name="${filter.id}" class="filter" type="radio" value="false" <c:choose><c:when test="${param[key] == null}">disabled="disabled"</c:when><c:when test="${param[key] == 'false'}">checked="checked"</c:when></c:choose>>
													</c:when>
													<c:when test="${filter.type == 'NUMERIC'}">
														<input id="${filter.id}" name="${filter.id}" class="filter large" type="number" required="required" <c:choose><c:when test="${param[key] == null}">disabled="disabled"</c:when><c:otherwise>value="${param[key]}"</c:otherwise></c:choose>>
													</c:when>
													<c:when test="${filter.type == 'FREE_TEXT'}">
														<input id="${filter.id}" name="${filter.id}" class="filter large" type="text" required="required" <c:choose><c:when test="${param[key] == null}">disabled="disabled"</c:when><c:otherwise>value="${param[key]}"</c:otherwise></c:choose>>
													</c:when>
													<c:when test="${filter.type == 'COMBO'}">
														<select id="${filter.id}" name="${filter.id}" <c:choose><c:when test="${fn:length(filter.options) < 4}">size="${fn:length(filter.options)}"</c:when><c:otherwise>size="4"</c:otherwise></c:choose> class="filter large" multiple="multiple" required="required" <c:if test="${param[key] == null}">disabled="disabled"</c:if>>
															<c:forEach var="option" items="${filter.options}">
																<option 
																	<c:forEach var="v" items="${paramValues[key]}">
																		<c:if test="${v == option.id}">
																			selected="selected"
																		</c:if>
																	</c:forEach>
																 value="${option.id}" title="${option.option}">${option.option}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${filter.type == 'DATE'}">
														<input id="${filter.id}" name="${filter.id}" class="datepicker filter large" type="text" required="required" <c:choose><c:when test="${param[key] == null}">disabled="disabled"</c:when><c:otherwise>value="${param[key]}"</c:otherwise></c:choose>>
													</c:when>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<input type="hidden" name="indicator" value="${param.indicator}">
				<input type="hidden" name="isquery" value="true">
				<input type="hidden" disabled="disabled" id="file-download-warning" value="Si la consulta que va a ejecutar devuelve muchos resultados considere usar la opción &quot;Consultar a archivo&quot;. Presione &quot;Aceptar&quot; para continuar con la ejecución o &quot;Cancelar&quot; para elegir la otra opción.">
				<c:if test="${fileDownloadWarningUsedKey != null}"><input type="hidden" disabled="disabled" id="file-download-warning-used"></c:if>
				<hr>
				<p class="center" id="submitarea">
					<button id="web" type="submit" name="submit" value="${requestTypeWeb}">Consultar</button>
					&nbsp;&nbsp;&nbsp;
					<button id="file" type="submit" name="submit" value="${requestTypeCsv}">Consultar a archivo</button><select id="formats" onchange="changeFormat(this);" class="formats" title="Seleccione el formato del archivo">
						<option value="${requestTypeCsv}">.csv&nbsp; (Texto plano)</option>
						<option value="${requestTypeOds}">.ods&nbsp; (OpenOffice/LibreOffice)</option>
						<option value="${requestTypeXlsx}">.xlsx (Microsoft Excel)</option>
					</select>
				</p>
			</form>
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
									<c:forEach var="cell" items="${row}">
										<td>${cell}</td>
									</c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="table-footer">Resultados:&nbsp;${tableSize}</div>
			</div>
                </c:if>
		</div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>
