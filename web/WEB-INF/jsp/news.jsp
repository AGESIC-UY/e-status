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
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
		<style>
			.panel {
				margin: auto;
				max-width: 1300px;
				padding: 25px;
			}
		</style>
	</head>
	<body>
		<div id="background1"></div>
		<div class="panel">
			<h1>Noticias <span class="logo"></span></h1>
			${newsContent}
			<p><a href="${continueURI}">Continuar</a><p>
		</div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>
