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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>e-status</title>
		<link href="//fonts.googleapis.com/css?family=Tulpen+One" rel="stylesheet" type="text/css">
		<link href="static/css/generic.css" rel="stylesheet" type="text/css">
		<link href="static/css/main.css" rel="stylesheet" type="text/css">
                <link href="static/css/jquery-ui/flick/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css">
		<link href="static/css/help.css" rel="stylesheet" type="text/css">
		<script src="static/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
                <script src="static/jquery/jquery-ui-1.10.4.custom.js" type="text/javascript"></script>
                <script src="static/jquery/jquery.ui.datepicker-es.js" type="text/javascript"></script>
		<script src="static/highcharts_4.0.1/js/highcharts.js" type="text/javascript"></script>
                <script src="static/highcharts_4.0.1/js/modules/exporting.js" type="text/javascript"></script>
                <script src="static/highcharts_4.0.1/js/themes/grid-light.js" type="text/javascript"></script>
		<script src="static/js/generic.js" type="text/javascript"></script>
		<script src="static/js/help.js" type="text/javascript"></script>
		
	</head>
	<body>
		<div id="background1"></div>
		<div class="panel">
			<h1>Ayuda</h1>
			<span class="helpcell">Contenido:</span>
			<ol class="helpcell">
				<li><a href="#inicio">Página de inicio</a></li>
				<li><a href="#historico">Evolución histórica</a></li>
				<li><a href="#grafica">Gráfica de evolución histórica</a></li>
				<li><a href="#detalle">Detalle de indicador</a></li>
				<li><a href="#csv">Importando un archivo csv</a></li>
				<!--<li><a href="#contact">Contacto</a></li>-->
			</ol>
			<h2 id="inicio">Página de inicio</h2>
			<div style="float:left;">
				<div class="pod">
					<div class="name-highlight"></div>
					<div class="name">Indicador</div>
					<div class="symbols-highlight"></div>
					<div class="symbols">
						<a title="Ver evolución del indicador" href="javascript:;">&#xe800;</a>
						<br>
						<a title="Ver detalle de los datos" href="javascript:;">&#xe801;</a>
					</div>
					<div class="val"><span class="val val-highlight">500</span>&nbsp;<span class="arrow arrow-highlight">▲</span></div>
					<div class="val sum"></div>
					<div class="taken-highlight"></div>
					<div class="taken">Actualizado: 15m</div>
					<div class="tag-highlight"></div>
					<div class="tag c10" title="Descripción del indicador."></div>
				</div>
			</div>
			<div class="helpcell">
				<p>En la página de inicio se muestran los indicadores.</p>
				<p>Los indicadores son los elementos básicos de información en <span class="logo"></span>. Cada indicador muestra un <span class="val-hover">número</span> que puede representar una cantidad, un promedio, un porcentaje u otro tipo de magnitud de algún dato en particular. El indicador mostrará en el <span class="name-hover">título</span> cuál es el dato que está representando y en la <span class="tag-hover">solapa de la esquina inferior izquierda</span> (dejando el cursor sobre ella) una breve descripción.</p>
				<p>Mientras se esté mostrando la página de inicio, los indicadores <span class="effect hover">actualizarán sus valores</span> de forma automática cada cierto período de tiempo (puede ser diferente para cada indicador) contra la base de datos de GURI. Cada indicador mostrará un <span class="arrow-hover">símbolo</span> "=", "▲" o "▼" cuando el nuevo valor haya permanecido igual, aumentado o disminuído (respectivamente) en relación al valor anterior. Es posible ver cuánto tiempo ha transcurrido desde la última actualización en la <span class="taken-hover">esquina inferior derecha.</span></p>
				<p>En la <span class="symbols-hover">esquina superior derecha</span> (dejando el cursor sobre el indicador) se encuentran las herramientas del indicador. Estas son la evolución histórica (<span style="font-family: symbols;">&#xe800;</span>) y el detalle (<span style="font-family: symbols;">&#xe801;</span>). No todos los indicadores disponen de la herramienta detalle. Aquellos que no la tengan no mostrarán el ícono correspondiente.</p>
			</div>
			<h2 id="historico">Evolución histórica</h2>
			<div style="float:right;">
				<div style="border: 1px solid #233e85;padding: 10px;width: 330px; margin: 20px;">
					<h3>Evolución histórica de indicador</h3>
					<h1>Indicador</h1>
					<p><span style="font-weight: bold;">Descripción:</span>&nbsp;Descripción del indicador.</p>
					<table id="filterstable"><tbody>
						<tr class="groupby-highlight">
							<td><label for="groupby">Consolidar por:&nbsp;</label></td>
							<td colspan="2"><select id="groupby" class="large" name="groupby">
								<option value="2">mes</option>
								<option value="1">día</option>
								<option value="0">no consolidar</option>
							</select></td>
						</tr>
						<tr class="groupusing-highlight">
							<td><label for="groupusing">Usando el valor:&nbsp;</label></td>
							<td colspan="2"><select id="groupusing" class="large" name="groupusing">
								<option value="AVG">promedio</option>
								<option value="MAX">máximo</option>
								<option value="MIN">mínimo</option>
							</select></td>
						<tr class="groupfrom-highlight">
							<td><span>Seleccionar valores desde:&nbsp;</span></td>
							<td><select id="monthfrom" class="large" name="monthfrom">
								<option value="1">mes</option>
							</select></td>
							<td><select id="dayfrom" name="dayfrom">
								<option value="1">día</option>
							</select></td>
						</tr>
						<tr class="groupfrom-highlight">
							<td><span>Hasta:&nbsp;</span></td>
							<td><select id="monthto" class="large" name="monthto"">
								<option value="1">mes</option>
							</select></td>
							<td><select id="dayto" name="dayto">
								<option value="1">día</option>
							</select></td>
						</tr>
						<tr class="year-highlight">
							<td><label>Año(s):&nbsp;</label></td>
							<td colspan="2"><select id="year" class="large" name="year" multiple="" required="" size="4">
								<option value="1">2014</option>
								<option value="2">2013</option>
								<option value="3">2012</option>
								<option value="4">2011</option>
								<option value="5">2010</option>
							</select></td>
						</tr>
					</tbody></table>
				</div>
			</div>
			<div class="helpcell">
				<p>En la página de evolución histórica se pueden visualizar todos los valores que ha tenido cada indicador desde que funcionó por primera vez.</p>
				<p>Cada vez que un indicador actualiza su valor, el valor anterior se guarda en la base de datos. Con esto es posible hacer un análisis de cómo ha evolucionado en el tiempo el dato que dicho indicador representa.</p>
				<p>Debido a que un indicador puede actualizarse varias veces por día, <span class="logo"></span> ofrece opciones de consolidación de los datos históricos para facilitar el análisis en períodos largos de tiempo. Por ejemplo: analizar la evolución de un indicador que se actualiza cada 15 minutos a lo largo de un mes desplegaría una tabla con alrededor de 2800 filas. Sería más cómodo consolidar el valor promedio de cada día y solo mostrar una fila para cada uno.</p>
				<ul>
					<li><span class="groupby-hover">Consolidación</span> por mes: de todos los datos recogidos, se calculará un único valor para cada mes utilizando la función seleccionada en <span class="groupusing-hover">Usando el valor</span>.</li>
					<li><span class="groupby-hover">Consolidación</span> por día: ídem anterior, pero calculando un valor por día en lugar de mes.</li>
					<li>Sin <span class="groupby-hover">consolidación</span>: si se elige la opción de no consolidar se mostrarán todos los valores registrados para ese indicador en el <span class="groupfrom-hover">período</span> seleccionado.</li>
				</ul>
				<p>A su vez, es posible consolidar mediante diferentes funciones. <span class="logo"></span> ofrece tres funciones de consolidación</p>
				<ul>
					<li><span class="groupusing-hover">Consolidación usando el valor</span> promedio: para los valores que pertenezcan al <span class="groupfrom-hover">período</span> seleccionado se calculará el valor promedio.</li>
					<li><span class="groupusing-hover">Consolidación usando el valor</span> máximo: para los valores que pertenezcan al <span class="groupfrom-hover">período</span> seleccionado se tomará el máximo de todos ellos.</li>
					<li><span class="groupusing-hover">Consolidación usando el valor</span> mínimo: para los valores que pertenezcan al <span class="groupfrom-hover">período</span> seleccionado se tomará el mínimo de todos ellos.</li>
				</ul>
				<p><span class="year-hover">La selección múltiple de años</span> permite mostrar datos de diferentes años (si están disponibles) en la misma tabla para facilitar la comparación de los mismos.</p>
				<p>El resultado de la consulta puede visualizarse en el explorador o exportarse a un archivo de planilla electrónica. Puede elegir entre los formatos ods (OpenOffice/LibreOffice), xlsx (Microsoft Excel) o texto separado por comas (csv). Si necesita ayuda abriendo un archivo csv revise la sección <a href="#csv">&quot;Importando un archivo csv&quot;</a>.</p>
			</div>
			<h2 id="grafica">Gráfica de evolución histórica</h2>
			<div class="helpcell" style="float: right; width: 30%;">
				<p>Luego de consultar los datos de la evolución histórica, <span class="logo"></span> desplegará una tabla con los valores además de una gráfica representando los mismos. La gráfica mostrará una línea para cada año (en caso de haber consultado más de uno) y es posible "navegar" sobre la misma con el cursor para ver el valor en un punto determinado.</p>
				<p>Si se desea, es posible volver a graficar los datos usando una escala diferente para el eje de las ordenadas en la sección <span class="chart-hover">Opciones de la gráfica</span>, ubicada debajo del botón "Consultar" en la página de evolución histórica.</p>
				<p>También es posible exportar la gráfica utilizando el menú ubicado en su esquina superior derecha.</p>
				<br><br><br>
				<table class="chart-highlight" style="margin: auto;">
					<thead>
						<tr>
							<th colspan="2">Opciones de la gráfica</th>
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
			</div>
			<div style="float: left; width: calc(70% - 60px); margin-bottom: 15px;">
				<table class="hidden" id="datatable">
					<thead>
						<tr>
							<th><div>Período</div></th>
							<th><div>2014</div></th>
							<th><div>2013</div></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><span class="visible">27/05</span><span class="hidden">2012-05-27</span></td>
							<td><span class="visible">500</span><span class="hidden">500.0</span></td>
							<td><span class="visible">600</span><span class="hidden">600.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">28/05</span><span class="hidden">2012-05-28</span></td>
							<td><span class="visible">520</span><span class="hidden">520.0</span></td>
							<td><span class="visible">650</span><span class="hidden">650.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">29/05</span><span class="hidden">2012-05-29</span></td>
							<td><span class="visible">543</span><span class="hidden">543.0</span></td>
							<td><span class="visible">650</span><span class="hidden">650.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">30/05</span><span class="hidden">2012-05-30</span></td>
							<td><span class="visible">530</span><span class="hidden">530.0</span></td>
							<td><span class="visible">701</span><span class="hidden">701.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">31/05</span><span class="hidden">2012-05-31</span></td>
							<td><span class="visible">500</span><span class="hidden">500.0</span></td>
							<td><span class="visible">740</span><span class="hidden">740.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">01/06</span><span class="hidden">2012-06-01</span></td>
							<td><span class="visible">2.350</span><span class="hidden">490.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">740.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">02/06</span><span class="hidden">2012-06-02</span></td>
							<td><span class="visible">2.350</span><span class="hidden">800.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">730.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">03/06</span><span class="hidden">2012-06-03</span></td>
							<td><span class="visible">2.350</span><span class="hidden">802.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">700.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">04/06</span><span class="hidden">2012-06-04</span></td>
							<td><span class="visible">2.351</span><span class="hidden">798.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">795.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">05/06</span><span class="hidden">2012-06-05</span></td>
							<td><span class="visible">2.351</span><span class="hidden">700.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">852.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">06/06</span><span class="hidden">2012-06-06</span></td>
							<td><span class="visible">2.351</span><span class="hidden">615.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">918.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">07/06</span><span class="hidden">2012-06-07</span></td>
							<td><span class="visible">2.351</span><span class="hidden">509.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1090.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">08/06</span><span class="hidden">2012-06-08</span></td>
							<td><span class="visible">2.351</span><span class="hidden">400.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1080.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">09/06</span><span class="hidden">2012-06-09</span></td>
							<td><span class="visible">2.352</span><span class="hidden">400.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1080.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">10/06</span><span class="hidden">2012-06-10</span></td>
							<td><span class="visible">2.352</span><span class="hidden">400.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1102.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">11/06</span><span class="hidden">2012-06-11</span></td>
							<td><span class="visible">2.352</span><span class="hidden">378.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1102.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">12/06</span><span class="hidden">2012-06-12</span></td>
							<td><span class="visible">2.352</span><span class="hidden">356.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">1100.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">13/06</span><span class="hidden">2012-06-13</span></td>
							<td><span class="visible">2.352</span><span class="hidden">321.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">109.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">14/06</span><span class="hidden">2012-06-14</span></td>
							<td><span class="visible">2.352</span><span class="hidden">278.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">0.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">15/06</span><span class="hidden">2012-06-15</span></td>
							<td><span class="visible">2.352</span><span class="hidden">450.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">0.0</span></td>
						</tr>
						<tr>
							<td><span class="visible">16/06</span><span class="hidden">2012-06-16</span></td>
							<td><span class="visible">2.352</span><span class="hidden">700.0</span></td>
							<td><span class="visible">2.347</span><span class="hidden">0.0</span></td>
						</tr>
					</tbody>
				</table>
				<div id="chart"></div>
			</div>
			<h2 id="detalle">Detalle de indicador</h2>
			<div style="width: 350px; float: left; border: 1px solid #233e85; padding: 10px; margin: 20px;" class="panel">
				<h3>Detalle de indicador</h3>
				<h1>Indicador</h1>
				<p><span style="font-weight: bold;">Descripción:</span>&nbsp;descripción del indicador.</p>
				<div class="table-row">
					<div class="position">
						<div class="scroll">
							<table id="filterstable" class="filter-highlight">
								<tbody>
									<tr>
										<td><input type="checkbox" class="fh" id="fh34"></td>
										<td><label for="fh34">Filtro por lista:&nbsp;</label></td>
										<td>
											<select disabled="disabled" required="required" multiple="multiple" class="filter large" size="4" name="34" id="34">
												<option title="Valor 1" value="146">Valor 1</option>
												<option title="Valor 2" value="147">Valor 2</option>
												<option title="Valor 3" value="148">Valor 3</option>
												<option title="Valor 4" value="149">Valor 4</option>
												<option title="Valor 5" value="150">Valor 5</option>
												<option title="Valor 6" value="151">Valor 6</option>
											</select>
										</td>
									</tr>
									<tr>
										<td><input type="checkbox" class="fh" id="fh42"></td>
										<td><label for="fh42">Filtro por texto:&nbsp;</label></td>
										<td>
											<input type="text" required="required" class="filter large" name="42" id="42" disabled="disabled">
										</td>
									</tr>
									<tr>
										<td><input type="checkbox" class="fh" id="fh43"></td>
										<td><label for="fh43">Filtro por fecha:&nbsp;</label></td>
										<td>
											<input type="text" class="datepicker filter" disabled="disabled" required="required" class="filter large" name="43" id="43">
										</td>
									</tr>

									<tr>
										<td><input type="checkbox" class="fh" id="fh44"></td>
										<td><label for="fh44">Filtro por verdadero/falso: </label></td>
										<td>
											<input type="checkbox" class="filter " name="44" id="44" disabled="disabled">
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<hr>
				<div class="center">
					<div class="submitweb-highlight" style="display: inline-block; padding: 5px;">
						<button value="w" name="submit" type="button" id="web">Consultar</button>
					</div>&nbsp;&nbsp;&nbsp;
					<div class="submitfile-highlight" style="display: inline-block; padding: 5px;">
						<button value="f" name="submit" type="button" id="file">Consultar a archivo</button><select id="formats" class="formats" title="Seleccione el formato del archivo">
							<option>.csv&nbsp; (Texto plano)</option>
							<option>.ods&nbsp; (OpenOffice/LibreOffice)</option>
							<option>.xlsx (Microsoft Excel)</option>
						</select>
					</div>
				</div>
			</div>
			<div class="helpcell">
				<p>La página de detalle permite ejecutar una consulta en tiempo real a la base de datos de GURI para obtener datos desagregados de un indicador. El nivel de desagregación y la información presentada puede ser diferente para cada indicador dependiendo de las necesidades del usuario. La consulta puede <span class="filter-hover">filtrarse por ciertos campos pre-definidos</span>. Al igual que con el formato de la consulta, los <span class="filter-hover">filtros</span> pueden generarse en base a las necesidades del usuario.</p>
				<p>Ejemplo: si el indicador representa la cantidad de escuelas públicas ingresadas en GURI, el detalle del mismo podría listar las escuelas con su Departamento, Número, Domicilio, etc.</p>
				<p>El resultado de la consulta puede <span class="submitweb-hover">visualizarse en el explorador</span> o <span class="submitfile-hover">exportarse a un archivo</span> de planilla electrónica. Puede elegir entre los formatos ods (OpenOffice/LibreOffice), xlsx (Microsoft Excel) o texto separado por comas (csv). Si necesita ayuda abriendo un archivo csv revise la sección <a href="#csv">&quot;Importando un archivo csv&quot;</a>.</p>
				<p><span style="font-weight: bold;">Atención</span>: las consultas de la página de detalle se ejecutan directamente contra el servidor de GURI en producción. Algunas de estas consultas pueden requerir una cantidad de procesamiento considerable por lo que se solicita no hacer un uso excesivo de las mismas para no afectar la operativa del sistema GURI. Por ejemplo: si se quiere hacer un análisis de los resultados se recomienda guardar la consulta a un archivo en lugar de ejecutarla varias veces con filtros diferentes.</p>
			</div>
			<h2 id="csv">Importando un archivo csv</h2>
			<div class="helpcell">
				<p>Csv es el formato de planilla electrónica más simple y puede abrirse con cualquier aplicación de esa índole como Microsoft Excel, OpenOffice, LibreOffice, Google Docs, iWork y más.</p>
				<p>Los archivos csv generados por <span class="logo"></span> están codificados en UTF-8 y su delimitador de columnas es el caracter tabulador. Estas dos propiedades se deben tener en cuenta al momento de importar el archivo en una aplicación de planilla electrónica.</p>
				<p>En los links siguientes puede ver detalladamente los pasos a seguir para abrir un archivo csv en las dos aplicaciones de planilla electrónica de uso más frecuente.</p>
				<ul>
					<li><a href="https://help.libreoffice.org/Calc/Importing_and_Exporting_CSV_Files/es#Para_abrir_un_Archivo_CSV_en_Calc">Importar el archivo en LibreOffice/OpenOffice</a></li>
					<li><a href="http://office.microsoft.com/es-es/excel-help/importar-o-exportar-archivos-de-texto-HP010099725.aspx#BMimport_data_from_a_text_file_by_openi">Importar el archivo en Microsoft Excel</a></li>
				</ul>
			</div>
			<!--<h2 id="contact">Contacto</h2>-->
			<!-- mibew button -->
				<!--<a href="http://mdai.cep.edu.uy:10016/mibew/client.php?locale=es&amp;group=1" target="_blank" onclick="if(navigator.userAgent.toLowerCase().indexOf('opera') != -1 &amp;&amp; window.event.preventDefault) window.event.preventDefault();this.newWindow = window.open(&#039;http://mdai.cep.edu.uy:10016/mibew/client.php?locale=es&amp;group=1&amp;url=&#039;+escape(document.location.href)+&#039;&amp;referrer=&#039;+escape(document.referrer), 'mibew', 'toolbar=0,scrollbars=0,location=0,status=1,menubar=0,width=640,height=480,resizable=1');this.newWindow.focus();this.newWindow.opener=window;return false;"><img src="http://mdai.cep.edu.uy:10016/mibew/b.php?i=guri&amp;lang=es&amp;group=1" border="0" alt=""/></a>-->
			<!-- / mibew button -->
		</div>
		<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
	</body>
</html>