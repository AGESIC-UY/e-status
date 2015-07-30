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
<%@page isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Error - Estatus</title>
		<link href="//fonts.googleapis.com/css?family=Tulpen+One" rel="stylesheet" type="text/css">
		<link href="static/css/generic.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div id="background1"></div>
                <div id="exception-container">
                    <div class="exception">
                        <h1>Error ${requestScope['javax.servlet.error.status_code']}</h1>
			<c:choose>
				<c:when test="${requestScope['javax.servlet.error.status_code'] == 400}">
					<p>Causa: la solicitud enviada al servidor es incorrecta.</p>
				</c:when>
				<c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
					<p>Causa: acceso denegado.</p>
				</c:when>
				<c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
					<p>Causa: la página solicitada no existe.</p>
				</c:when>
				<c:when test="${requestScope['javax.servlet.error.status_code'] == 405}">
					<p>Causa: el servidor no admite este método para este recurso.</p>
				</c:when>
				<c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
					<p>Causa: ${requestScope['javax.servlet.error.exception']}</p>
				</c:when>
			</c:choose>
                    </div>
                </div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>

