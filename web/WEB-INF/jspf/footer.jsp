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
<div id="footer">
	<div id="clock"></div>
	<a class="banner" href="https://guri.ceip.edu.uy/" target="_blank"><img class="banner" src="static/img/logo_guri.png"></a>
	<a class="banner" href="http://ceip.edu.uy/" target="_blank"><img class="banner" src="static/img/CEIP_membrete.png"></a>
	<c:if test="${user != null}">
	<div id="about">
		<a href=".">Inicio</a>&nbsp;|&nbsp;
		<a class="help" href="ayuda">Ayuda</a>&nbsp;|&nbsp;
		<a href="acercade">Acerca de</a>&nbsp;|&nbsp;
		<c:if test="${role == 'admin'}">
			<a href="admin">Admin</a>&nbsp;|&nbsp;
		</c:if>
		<a href="salir">Salir</a>
	</div>
	</c:if>
</div>
