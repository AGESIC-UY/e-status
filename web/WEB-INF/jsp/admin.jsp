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
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
	</head>
	<style>
		.red {
			background-color: red;
		}
		.yellow {
			background-color: yellow;
		}
		.header {
			cursor: pointer;
		}
		.panel {
			padding: 25px;
		}
		.f {
			font-weight: bold;
		}
		table {
			border-collapse: collapse;
		}
		table td, table th:not(:empty) {
			border: 1px solid #cbd3da;
			padding: 4px;
		}
		tfoot {
			text-align: center;
		}
		div#right {
			float: right;
			padding: 25px;
		}
		div#indicator-table {
			padding: 25px;
		}
		div#rel-container {
			padding: 25px;
			overflow-x: auto;
		}
	</style>
	<script>
		function toggleColumn(elem, index) {
			if (jQuery(elem).prop('checked')) {
				jQuery('td > input[type="checkbox"]:eq(' + index + ')', jQuery('table#reluserindicator tr')).prop('checked', true);
			} else {
				jQuery('td > input[type="checkbox"]:eq(' + index + ')', jQuery('table#reluserindicator tr')).prop('checked', false);
			}
			
		}
		function toggleRow(elem) {
			if (jQuery(elem).prop('checked')) {
				jQuery('td > input[type="checkbox"]', elem.parentElement.parentElement).prop('checked', true);
			} else {
				jQuery('td > input[type="checkbox"]', elem.parentElement.parentElement).prop('checked', false);
			}
		}
		function paintColumn(elem, index) {
			if (!jQuery(elem).hasClass('yellow')) {
				jQuery(elem).addClass('yellow');
				jQuery('td:eq(' + index + ')', jQuery('table#reluserindicator tr')).addClass('yellow');
			} else {
				jQuery(elem).removeClass('yellow');
				jQuery('td:eq(' + index + ')', jQuery('table#reluserindicator tr')).removeClass('yellow');
			}
			
		}
		function paintRow(elem) {
			if (!jQuery(elem).hasClass('yellow')) {
				jQuery(elem).addClass('yellow');
				jQuery('td', elem.parentElement).addClass('yellow');
			} else {
				jQuery(elem).removeClass('yellow');
				jQuery('td', elem.parentElement).removeClass('yellow');
			}
		}
		var newWindow;
		function popup() {
			if (typeof(newWindow) == 'undefined') {
				newWindow = window.open('about:blank', 'newWindow', 'height=200,width=200');
			}
			var content = jQuery('#indicator-table').html();
			newWindow.document.open();
			newWindow.document.write(content);
			newWindow.document.close();
			newWindow.focus();
		}
	</script>
	<body>
		<div id="background1"></div>
		<div class="panel">
			<h1>Administración</h1>
			<form method="POST" action="admin">
				<div id="rel-container">
					<h2>Asignación de indicadores a usuarios</h2>
					<a href="#indicator-table" onclick="">Ver tabla de indicadores</a>
					<table id="reluserindicator">
						<thead>
							<tr>
								<th></th>
								<th></th>
								<c:forEach items="${indicators}" var="indicator" varStatus="forStatus">
									<th class="header" title="<c:out value="${indicator[1]}"/>" onclick="paintColumn(this, ${forStatus.index})"><c:out value="${indicator[0]}"/></th>
								</c:forEach>
							</tr>
							<tr>
								<th></th>
								<th title="Marcado masivo" class="red">?</th>
								<c:forEach items="${indicators}" var="indicator" varStatus="forStatus">
									<th class="red"><input onchange="toggleColumn(this, ${forStatus.index});" type="checkbox"></th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${users}" var="user" varStatus="forStatus">
								<c:set var="userIndex">${forStatus.index}</c:set>
								<tr>
									<th title="<c:out value="${user[0]}"/>" class="header" onclick="paintRow(this)"><c:choose><c:when test="${user[1] == null}"><c:out value="${user[0]}"/></c:when><c:otherwise><c:out value="${user[1]}"/></c:otherwise></c:choose></th>
									<th class="red"><input onchange="toggleRow(this);" type="checkbox"></th>
									<c:forEach items="${relUserIndicator[userIndex]}" var="rel" varStatus="forStatus2">
										<c:set var="indicatorIndex">${forStatus2.index}</c:set>
										<td><input name="${indicators[indicatorIndex][0]}-${user[0]}" type="checkbox"<c:if test="${rel}"> checked="checked"</c:if>></td>
									</c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<p class="center"><button type="submit" name="u">Guardar</button></p>
			</form>
			<div id="right">
				<h2>Usuarios</h2>
				<table>
					<c:forEach items="${users}" var="user">
						<tr>
							<th><c:out value="${user[1]}"/>&nbsp;(<c:out value="${user[2]}"/>)</th>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div id="indicator-table">
				<h2>Tabla de indicadores</h2>
				<table>
					<thead>
						<tr>
							<th>Indicador</th>
							<th>Identificador</th>
							<th>Descripción</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${indicators}" var="indicator" varStatus="forStatus">
							<tr>
								<td><c:out value="${indicator[1]}"/></td>
								<td><c:out value="${indicator[0]}"/><c:if test="${indicator[3] == 'false'}">*</c:if></td>
								<td><c:out value="${indicator[2]}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<p class="f">*: Indicador inactivo.</p>
			</div>
			<br style="clear:both;">
		</div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>
