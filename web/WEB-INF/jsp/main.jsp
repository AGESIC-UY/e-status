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
		<link href="static/css/main.css" rel="stylesheet" type="text/css">
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/jquery/jquery.timeago.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
		<script src="static/js/main.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="background1"></div>
		<div id="pods">
			<c:forEach items="${indicators}" var="i">
				<div id="${i.id}" class="pod">
					<div class="name"><c:out value="${i.name}"/></div>
					<div class="symbols">
						<a title="Ver evolución del indicador" href="historico?indicator=${i.id}">&#xe800;</a>
						<br>
						<c:if test="${i.showDetail}"><a title="Ver detalle de los datos" href="detalle?indicator=${i.id}">&#xe801;</a></c:if>
					</div>
					<div class="val"><span class="val">${i.formattedVal}</span>&nbsp;<span class="arrow"><c:choose><c:when test="${i.tendency == 0}">=</c:when><c:when test="${i.tendency > 0}">▲</c:when><c:when test="${i.tendency < 0}">▼</c:when></c:choose></span></div>
					<div class="val sum"></div>
					<div class="taken">Actualizado: <time class="timeago" datetime="${i.formattedTaken}"></time></div>
					<div class="tag c${i.classification}" title="<c:out value="${i.explanation}"/>"></div>
				</div>
			</c:forEach>
		</div>
		<div id="exception-container" class="hidden">
			<div class="exception">
				<p><span class="sadface"></span></p>
				<p>Parece que se ha interrumpido la comunicación con <span class="logo"></span>.</p>
				<p>Verifique la conexión a internet y recargue la página.</p>
			</div>
		</div>
		<input type="hidden" id="ajax_period" value="${ajax_period}">
		<input type="hidden" id="interval_id">
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>
