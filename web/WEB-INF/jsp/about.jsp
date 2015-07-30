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
		<link href="static/css/about.css" rel="stylesheet" type="text/css">
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
		<script>
			function animatePod() {
				var val = parseInt(jQuery('span.val').html()) + (Math.random() < 0.5 ? -1 : 1) * parseInt(Math.random() * 50);
				var row = jQuery('#cartoon-container');
				var oldVal = jQuery('span.val', row).html();
				jQuery('span.val', row).html(val);
				if (oldVal != val) {
					playSumEffect(row, oldVal);
				}
				if (val - oldVal < 0) {
					jQuery('.arrow', row).html('▼');
				} else if (val - oldVal > 0) {
					jQuery('.arrow', row).html('▲');
				} else {
					jQuery('.arrow', row).html('=');
				}
			}
			setTimeout(function(){animatePod();}, 1600);
			setTimeout(function(){setInterval(function(){animatePod();}, 4000);}, 1600);
		</script>
	</head>
	<body>
		<div id="background1"></div>
		<div class="panel">
			<h1 class="logo"></h1>
			<p><span class="logo"></span> es una aplicación diseñada para el rápido análisis de los datos ingresados en el Sistema de Gestión Unificada de Registros e Información: GURI. La interfaz de <span class="logo"></span> está pensada para mostrar aquellos datos que el usuario considere relevantes en una forma consolidada. Todas las modificaciones hechas a los datos de GURI se verán impactadas en <span class="logo"></span> en cuestión de minutos.</p>
			<p><span class="logo"></span> mantiene una base de datos histórica independiente de GURI con toda la información que ha recopilado de dicho sistema. Esta información puede consultarse y graficarse en la misma aplicación.</p>
			<p>Si necesita alguna información no incluída en <span class="logo"></span> y que esté registrada en GURI, es posible realizar la consulta directamente de la base de datos. Para ello deberá ser solicitado al grupo de trabajo del programa (correo electrónico: <a href="mailto:solicitud-estatus@guri.ceip.edu.uy">solicitud-estatus@guri.ceip.edu.uy</a>).</p>
			<div id="cartoon-container">
				<div class="val"><span class="val val-highlight">100</span>&nbsp;<span class="arrow arrow-highlight">▲</span></div>
				<div class="val sum"></div>
				<img id="img-base" src="static/img/estatus_1.png">
				<img class="cartoon" id="img-ball1" src="static/img/estatus_2.png">
				<img class="cartoon" id="img-ball2" src="static/img/estatus_2.png">
				<img class="cartoon" id="img-ball3" src="static/img/estatus_2.png">
				<img class="cartoon" id="img-cable1" src="static/img/estatus_3.png">
				<img class="cartoon" id="img-cable2" src="static/img/estatus_4.png">
			</div>
			<p><span class="logo"></span> fue ideado y desarrollado por el equipo de programación de GURI.</p>
			<h2>Equipo:</h2>
			<ul>
				<li>Javier Ayres</li>
				<li>Guillermo Ettlin</li>
				<li>Juan Miguel Martí</li>
				<li>Oscar Montañés</li>
			</ul>
			<h2>Versión</h2>
			<p>r<c:out value="${revision}"/></p>
		</div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>
