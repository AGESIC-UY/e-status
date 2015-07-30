<!DOCTYPE html>
<html>
	<head>
		<title>Instalación y configuración de e-status</title>
		<meta charset="UTF-8">
		<style>
			td, th {
				border: 1px solid black;
			}
			table {
				border-collapse: collapse;
			}
			.code {
				font-family: monospace;
			}
		</style>
	</head>
	<body>
		<h1>Instalación y configuración de e-status</h1>
		<h2>Índice</h2>
		<ol>
			<li><a href="#1">Introducción</a></li>
			<li><a href="#2">Estructura de carpetas</a></li>
			<li><a href="#3">Requerimientos</a></li>
			<li><a href="#4">Creación de la base de datos local</a></li>
			<li><a href="#5">Configuración de la aplicación</a></li>
			<li><a href="#6">Configuración de CAS</a></li>
			<li><a href="#7">Instalación y prueba</a></li>
			<li><a href="#8">Creación de indicadores</a></li>
			<li><a href="#9">Agregar "detalle" a un indicador</a></li>
			<li><a href="#10">Crear filtros para consultas de "detalle"</a></li>
			<li><a href="#11">Configurar la actualización automática</a></li>
			<li><a href="#12">Ayuda al usuario final</a></li>
			<li><a href="#13">Personalización y consideraciones finales</a></li>
		</ol>
		<h2 id="1">Introducción</h2>
		<p>e-status es una aplicación java web que actúa como panel de visualización de información. Funciona con dos accesos a bases de datos, una siendo la base local de e-status (donde guardará usuarios y configuraciones) y otra siendo la base "remota" en donde se encuentra la información a consultar.</p>
		<p>La aplicación consume datos de la base remota en base a "indicadores" que el encargado de administrar la aplicación deberá definir. Básicamente cada indicador tiene un nombre y una consulta SQL que se ejecutará contra la base remota. En este manual se detallará la instalación de la aplicación y la creación de indicadores.</p>
		<p>e-status está licenciado bajo la GPLv3 (<span class="code">e-status/LICENSE.txt</span>).</p>
		<h2 id="2">Estructura de carpetas</h2>
		<pre>
	e-status
	├── extra (scripts de instalación, configuración y este archivo de ayuda)
	├── lib (librerías externas)
	├── nbproject (carpeta de proyecto de Netbeans)
	├── src (código fuente java)
	│   ├── conf
	│   └── java
	│       └── uy
	│           └── edu
	│               └── ceip
	│                   └── estatus
	│                       ├── db
	│                       ├── filters
	│                       └── servlets
	└── web (jsp, javascript, estilos y otros recursos estáticos)
	    ├── META-INF
	    ├── static
	    │   ├── css
	    │   ├── font
	    │   ├── highcharts_4.0.1
	    │   ├── img
	    │   ├── jquery
	    │   ├── js
	    │   └── spin
	    └── WEB-INF
		├── jsp
		│   └── errors
		└── jspf
		</pre>
		<h2 id="3">Requerimientos</h2>
		<table>
			<thead>
				<tr>
					<th>Requerimiento</th>
					<th>Solución utilizada</th>
					<th>Observaciones</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Java</td>
					<td>OpenJDK 7</td>
					<td>No requiere OpenJDK pero la versión de Java debe ser 7 o superior.</td>
				</tr>
				<tr>
					<td>Servlet container</td>
					<td>Tomcat 7</td>
					<td>No se ha probado con otras soluciones pero no tiene dependencias de Tomcat.</td>
				</tr>
				<tr>
					<td>Base de datos</td>
					<td>PostgreSQL 9.3+</td>
					<td>No se ha probado con otro motor de base de datos pero no usa funcionalidades exclusivas de PostgreSQL. En caso usar otro motor se deberá cambiar, como mínimo, el JDBC y el script de creación de la base de datos.</td>
				</tr>
				<tr>
					<td>Single Sign-on</td>
					<td>CAS 4.0.0+</td>
					<td>e-status no realiza autenticación de usuarios por su cuenta, pero puede adaptarse para ello. En su defecto, CAS 4.0.0 o superior es necesario.</td>
				</tr>
				<tr>
					<td>Entorno de desarrollo</td>
					<td>Netbeans 8</td>
					<td>La aplicación se entrega como un proyecto de Netbeans pero debería poder importarse en cualquier IDE.</td>
				</tr>
				<tr>
					<td>Planificador de tareas + aplicación que realice peticiones HTTP</td>
					<td>cron + curl</td>
					<td>Cualquier planificador de tareas en conjunto con una aplicación capaz de realizar peticiones HTTP es suficiente.</td>
				</tr>
			</tbody>
		</table>
		<h2 id="4">Creación de la base de datos local</h2>
		<p>En caso de no utilizar PostgreSQL es posible que el script de instalación requiera alguna modificación.</p>
		<ol>
			<li>Ejectuar el archivo de creación de usuarios <span class="code">e-status/extra/users.sql</span>. Los usuarios son creados sin contraseña, esta tarea deberá ser realizada manualmente por el administrador.</li>
			<li>Crear la base de datos. Se debe tener en cuenta el nombre de la misma para el apartado <a href="#5">Configuración de la aplicación</a>.</li>
			<li>Conectado a la nueva base, ejectuar el archivo de creación del esquema <span class="code">e-status/extra/create_schema.sql</span>.</li>
			<li>Insertar los parámetros necesarios ejecutando el archivo <span class="code">e-status/extra/insert_parameters.sql</span>. Más adelante se le asignarán valores a cada uno.</li>
			<li>Ejectuar el archivo de creación de roles <span class="code">e-status/extra/insert_roles.sql</span>.</li>
		</ol>
		<h2 id="5">Configuración de la aplicación</h2>
		<p>e-status mantiene toda su configuración en la tabla <span class="code">parameters</span> en su base de datos local, excepto por un conjunto mínimo de propiedades que necesita antes de poder conectarse a dicha base. Estas propiedades se encuentran en el archivo <span class="code">e-status/src/java/estatus.properties</span>.</p>
		<ul>
			<li><span class="code">db_host</span>: IP o hostname del servidor de base de datos.</li>
			<li><span class="code">db_name</span>: nombre de la base de datos local (creada en el paso 2 del apartado <a href="#4">Creación de la base de datos local</a>).</li>
			<li><span class="code">db_port</span>: puerto en cual escucha el servidor de base de datos.</li>
			<li><span class="code">estatus_user</span>: nombre del usuario estatus (el script de instalación lo crea con nombre "estatus", pero puede ser otro). Este es el usuario que la aplicación utiliza cuando necesita actualizar datos de forma automática.</li>
			<li><span class="code">estatus_password</span>: contraseña del usuario anterior.</li>
			<li><span class="code">normal_user</span>: usuario normal (el script de instalación lo crea con nombre "estatus_user", pero puede ser otro). Todos los usuarios con permisos normales se conectarán a la base de datos usando este usuario.</li>
			<li><span class="code">normal_user_password</span>: contraseña del usuario anterior.</li>
			<li><span class="code">admin_user</span>: usuario administrador (el script de instalación lo crea con nombre "estatus_admin", pero puede ser otro). Todos los usuarios con permisos de administración se conectarán a la base de datos usando este usuario.</li>
			<li><span class="code">admin_user_password</span>: contraseña del usuario anterior.</li>
			<li><span class="code">revision</span>: versión de la aplicación.</li>
		</ul>
		<p>Ahora procederemos a darle valores a los parámetros en la base de datos (tabla <span class="code">parameters</span>):</p>
		<ul>
			<li><span class="code">host</span>: IP o hostname del servidor de base de datos remoto que actuará como fuente de datos.</li>
			<li><span class="code">port</span>: puerto en cual escucha el servidor de base de datos.</li>
			<li><span class="code">user</span>: nombre del usuario utilizado para extraer información de la base de datos remota.</li>
			<li><span class="code">password</span>: contraseña del usuario anterior.</li>
			<li><span class="code">name</span>: nombre de la base de datos remota.</li>
			<li><span class="code">ajax_period</span>: período de tiempo (en milisegundos) cada cual la página principal deberá actualizarse automáticamente.</li>
			<li><span class="code">news_content</span>: e-status puede mostrar una "noticia" a los usuarios luego de loguearse. El contenido (en HTML) se ingresa en este parámetro y funciona en conjunto con la columna <span class="code">must_see_news</span> de la tabla <span class="code">local_user</span>.</li>
		</ul>	
		<h2 id="6">Configuración de CAS</h2>
		<p>En el archivo <span class="code">e-status/web/WEB-INF/web.xml</span> ubicar la siguiente sección:</p>
		<pre>
	&lt;filter&gt;
		&lt;filter-name&gt;CAS Authentication Filter&lt;/filter-name&gt;
		&lt;filter-class&gt;org.jasig.cas.client.authentication.AuthenticationFilter&lt;/filter-class&gt;
		&lt;init-param&gt;
			&lt;param-name&gt;casServerLoginUrl&lt;/param-name&gt;
			&lt;param-value&gt;https://localhost:8443/cas/login&lt;/param-value&gt;
		&lt;/init-param&gt;
		&lt;init-param&gt;
			&lt;param-name&gt;service&lt;/param-name&gt;
			&lt;param-value&gt;https://localhost:8443/estatus/&lt;/param-value&gt;
		&lt;/init-param&gt;
	&lt;/filter&gt;
	&lt;filter&gt;
		&lt;filter-name&gt;CAS Validation Filter&lt;/filter-name&gt;
		&lt;filter-class&gt;org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter&lt;/filter-class&gt;
		&lt;init-param&gt;
			&lt;param-name&gt;casServerUrlPrefix&lt;/param-name&gt;
			&lt;param-value&gt;https://localhost:8443/cas/p3&lt;/param-value&gt;
		&lt;/init-param&gt;
		&lt;init-param&gt;
			&lt;param-name&gt;serverName&lt;/param-name&gt;
			&lt;param-value&gt;https://localhost:8443&lt;/param-value&gt;
		&lt;/init-param&gt;
	&lt;/filter&gt;
	&lt;filter&gt;
		&lt;filter-name&gt;CAS HttpServletRequest Wrapper Filter&lt;/filter-name&gt;
		&lt;filter-class&gt;org.jasig.cas.client.util.HttpServletRequestWrapperFilter&lt;/filter-class&gt;
	&lt;/filter&gt;
	&lt;filter&gt;
		&lt;filter-name&gt;CAS Assertion Thread Local Filter&lt;/filter-name&gt;
		&lt;filter-class&gt;org.jasig.cas.client.util.AssertionThreadLocalFilter&lt;/filter-class&gt;
	&lt;/filter&gt;
		</pre>
		<p>Hay 4 parámetros que incluyen valores por defecto asumiendo una instalación de CAS y e-status en el mismo servidor, y siendo los context path de cada uno <span class="code">/cas</span> y <span class="code">/estatus</span> respectivamente. Se deberán modificar estos parámetros según las características de la infraestructura local:</p>
		<ul>
			<li>
				<span class="code">CAS Authentication Filter</span><br>
				<ul>
					<li><span class="code">casServerLoginUrl</span>: URL del servicio de autenticación de CAS (mantener la ruta /login).</li>
					<li><span class="code">service</span>: URL de la aplicación e-status.</li>
				</ul>
			</li>
			<li>
				<span class="code">CAS Validation Filter</span><br>
				<ul>
					<li><span class="code">casServerUrlPrefix</span>: URL del servicio de validación de CAS (mantener la ruta /p3).</li>
					<li><span class="code">serverName</span>: URL del servidor donde está instalado e-status (sin context path).</li>
				</ul>
			</li>		
		</ul>
		<p>Si el servidor de CAS tiene un certificado "self-signed" se deberá importar el mismo a la TrustStore del servidor en donde e-status está instalado. Por ejemplos de este requerimiento u otros problemas con la configuración de CAS referirse a los siguientes documentos:</p>
		<ul>
			<li><a href="https://wiki.jasig.org/display/CASUM/SSL+Troubleshooting+and+Reference+Guide">https://wiki.jasig.org/display/CASUM/SSL+Troubleshooting+and+Reference+Guide</a></li>
			<li><a href="https://wiki.jasig.org/display/CASC/Configuring+the+Jasig+CAS+Client+for+Java+in+the+web.xml">https://wiki.jasig.org/display/CASC/Configuring+the+Jasig+CAS+Client+for+Java+in+the+web.xml</a></li>
		</ul>
		<p>Luego es necesario indicarle al servlet de e-status <span class="code">Exit</span> la URL del servicio de cierre de sesión de CAS. Ubicar la siguiente sección:</p>
		<pre>
	&lt;servlet&gt;
		&lt;servlet-name&gt;Exit&lt;/servlet-name&gt;
		&lt;servlet-class&gt;uy.edu.ceip.estatus.servlets.Exit&lt;/servlet-class&gt;
		&lt;init-param&gt;
			&lt;param-name&gt;casServerLogoutUrl&lt;/param-name&gt;
			&lt;param-value&gt;https://localhost:8443/cas/logout&lt;/param-value&gt;
		&lt;/init-param&gt;
	&lt;/servlet&gt;
		</pre>
		<p>En ella, reeplazar la URL del servicio de autenticación de CAS (mantener la ruta /logout).</p>
		<p>Por último, migrar los usuarios necesarios a la tabla <span class="code">local_user</span> con el rol deseado.</p>
		<h2 id="7">Instalación y prueba</h2>
		<p>Construir el war usando el método preferido. En caso de no usar Ant probablemente sea necesario indicar las dependencias del proyecto: las mismas se encuentran en la carpeta <span class="code">e-status/lib</span>.</p>
		<p>Hacer el deploy en el servlet container e ingresar. Debería verse un imagen de fondo (puede reemplazarse por otra en <span class="code">e-status/web/static/img/background1.jpg</span>) y un footer.</p>
		<h2 id="8">Creación de indicadores</h2>
		<p>Los indicadores son las únidades básicas de información de e-status. Se muestran al usuario como rectángulos en la pantalla principal con un número dentro, resultado de una consulta SQL.</p>
		<p>Los indicadores se componen por una consulta SQL que como resultado debe devolver una fila con una columna de tipo numérico (la consulta principal), un nombre, una segunda consulta SQL (opcional) que actúa como desagregación de la consulta principal, y varios parámetros de configuración. Técnicamente, un indicador no es más que un registro en la tabla <span class="code">indicator</span>. A continuación se detallan todas las columnas de dicha tabla:</p>
		<ul>
			<li><span class="code">name</span>: nombre del indicador</li>
			<li><span class="code">query</span>: consulta principal. Debe devolver una fila con una columna de tipo numérico.</li>
			<li><span class="code">active</span>: variable de actividad del indicador. Si está en falso, el indicador no actualizará sus datos ni se mostrará a los usuarios.</li>
			<li><span class="code">val_is_integer</span>: indica si el valor devuelto por la consulta principal es un número entero. El formato con que se muestre el número en pantalla dependerá de este valor.</li>
			<li><span class="code">val_is_percentage</span>: indica si el valor devuelto por la consulta principal es un porcentaje. El formato con que se muestre el número en pantalla dependerá de este valor.</li>
			<li><span class="code">update_interval</span>: período de actualización en minutos del indicador.</li>
			<li><span class="code">classification</span>: los indicadores se ordenarán en pantalla según este valor en orden alfabético. Este valor también es el que determina el color de la etiqueta del indicador según los estilos definidos en <span class="code">e-status/web/static/css/main.css</span> (buscar el comentario "Tag colors").</li>
			<li><span class="code">explanation</span>: descripción del dato que está mostrando el indicador.</li>
			<li><span class="code">detail</span>: consulta de detalle. Esta consulta no tiene restricciones en cuanto a filas o columnas y, aunque es una consulta totalmente independiente de la principal, su objetivo es actuar como un nivel de desagregación de la misma. Esta consulta se ejecuta contra la base remota a demanda del usuario. Si no se desea tener consulta de detalle en cierto indicador, dejar esta columna con valor <span class="code">NULL</span>.</li>
		</ul>
		<h2 id="9">Agregar "detalle" a un indicador</h2>
		<p>Tal como se explicó en el apartado anterior, los indicadores pueden tener una consulta adicional denominada "consulta de detalle". Esta consulta puede devolver cualquier cantidad de filas o columnas y se ejcutará a demanda del usuario contra la base remota.</p>
		<p>La consulta de detalle se guarda en la columna <span class="code">detail</span> de la tabla <span class="code">indicator</span>. Si su valor es <span class="code">NULL</span>, e-status entiende que el indicador no tiene detalle y no muestra al usuario el botón correspondiente.</p>
		<p>La única restricción de la consulta de detalle es la "filters wildcard". La "filters wildcard" no es más que un comentario ubicado en el texto de la consulta el cual será reemplazado por varias cláusulas <span class="code">AND</span> obtenidas de los filtros indicados por el usuario (los filtros se describen en el apartado siguiente). La "filters wildcard" debe ser el comentario <span class="code">--filters</span> y debe estar ubicado luego de una cláusula <span class="code">WHERE</span>. Si la consulta no tiene necesidad de una cláusula <span class="code">WHERE</span>, deberá agregarse una con la "filters wildcard" de la siguiente manera: <span class="code">WHERE TRUE --filters</span>.</p>
		<h2 id="10">Crear filtros para consultas de "detalle"</h2>
		<p>Las consultas de detalle pueden tener filtros definidos por el administrador de la aplicación. Hay tres tablas relacionadas con los filtros:</p>
		<ul>
			<li><span class="code">filter</span>: definición del filtro. A continuación se listan las columnas.<br>
				<ul>
					<li><span class="code">name</span>: nombre del filtro.</li>
					<li><span class="code">filter_type</span>: identificador del tipo del filtro. Los tipos de filtros se encuentran definidos en <span class="code">e-status/src/java/uy/edu/ceip/estatus/FilterType.java</span>.</li>
					<li><span class="code">field_name</span>: nombre de la columna en la tabla de destino (base remota) que debe filtrarse.</li>
					<li><span class="code">filter_operator</span>: identificador del operador. Los operadores de filtros se encuentran definidos en <span class="code">e-status/src/java/uy/edu/ceip/estatus/FilterOperator.java</span>. Tener en cuenta que los operadores disponibles dependen del tipo de filtro, referirse al método <span class="code">buildExpression</span> del archivo <span class="code">e-status/src/java/uy/edu/ceip/estatus/DetailFilter.java</span> para ver las combinaciones válidas.</li>
					<li><span class="code">filter_order</span>: define el orden en que se mostrarán los filtros en la página, de menor a mayor.</li>
				</ul>
			</li>
			<li><span class="code">filter_option</span>: opciones disponibles de un filtro de tipo <span class="code">COMBO</span>. A continuación se listan las columnas.<br>
				<ul>
					<li><span class="code">filter</span>: referencia al filtro al cual pertenecen esta opción. El filtro debe ser de tipo <span class="code">COMBO</span>.</li>
					<li><span class="code">option_value</span>: texto de la opción. El filtro comparará por igualdad el valor de la columna de destino con cada texto de opción.</li>
				</ul>
			</li>
			<li><span class="code">rel_filter_indicator</span>: definición de qué filtros deben estar disponibles para cada indicador.</li>
		</ul>
		<h2 id="11">Configurar la actualización automática</h2>
		<p>e-status depende de alguna herramienta externa para actualizar de forma automática sus indicadores.</p>
		<p>El método consiste en realizar periódicamente una petición a una URL especial de e-status que invocará las rutinas necesarias para que todos los indicadores que lo necesiten se actualicen. La URL en particular es <span class="code">/actualizar</span>. Se debe tener en cuenta que esta URL no está protegida por autenticación para facilitar la petición, pero existe un filtro en e-status que solo permite que esta URL sea accedida desde el mismo host en donde la aplicación está instalada. El filtro en cuestión es <span class="code">e-status/src/java/uy/edu/ceip/estatus/filters/UpdateFromLocalhost.java</span> y si el administrador así lo quisiera puede desactivarse, pero se debe considerar que esta URL no debe ser accedida por los usuarios de la aplicación.</p>
		<p>La automatización funciona de la siguiente manera:</p>
		<ol>
			<li>Se obtienen todos los indicadores activos de la base de datos.</li>
			<li>Se filtran aquellos que no necesiten actualizarse según su <span class="code">update_interval</span> y la última vez que fue actualizado (e-status guarda este dato).</li>
			<li>Se ejecuta la consulta del indicador contra la base remota.</li>
			<li>Se guarda el nuevo valor junto con la fecha y hora del momento en que se actualizó.</li>
		</ol>
		<p>El período utilizado para la planificación de la tarea programada debe definirse considerando el indicador con menor <span class="code">update_interval</span>. Si el menor <span class="code">update_interval</span> es de 10 minutos, entonces la tarea deberá programarse al menos cada 10 minutos. A continuación se muestra un ejemplo usando cron y curl, un período de 15 minutos y una instalación de e-status usando el context path <span class="code">/estatus</span>.</p>
		<p><span class="code">0,15,30,45 * * * * curl -3 -k https://localhost:8443/estatus/actualizar</span></p>
		<p><span style="font-weight: bold;">Aclaración</span>: este período de actualización es diferente al parámetro <span class="code">ajax_period</span> descripto en <a href="#5">Configuración de la aplicación</a>. Este primero define cada cuánto tiempo el servidor de e-status deberá actualizar sus indicadores contra la base remota, mientras que el segundo define cada cuánto tiempo la página principal de e-status enviará una petición ajax al servidor de e-status (es decir, no implica interacción con la base remota) para mostrar los valores actualizados de los indicadores (si hubiese alguno).</p>
		<h2 id="12">Ayuda al usuario final</h2>
		<p>La aplicación incluye un manual de uso completo para los usuarios finales, accesible desde la propia interfaz.</p>
		<h2 id="13">Personalización y consideraciones finales</h2>
		<p>Más allá de que su funcionamiento es bastante genérico y puede servir a distintos casos de uso, e-status se entrega como una aplicación hecha por y para el CEIP. Esto implica que hay varias referencias al organismo pero todas son fácilmente modificables:</p>
		<ul>
			<li>El fondo de pantalla de la página principal ubicado en <span class="code">e-status/web/static/img/background1.jpg</span>.</li>
			<li>El fondo de pantalla de las páginas de histórico y detalle, ubicado en <span class="code">e-status/web/static/img/background2.jpg</span>.</li>
			<li>Los links a GURI y CEIP del footer, estando el código del footer ubicado en <span class="code">e-status/web/WEB-INF/jspf/footer.jsp</span> y las imágenes correspondientes en <span class="code">e-status/web/static/img/logo_guri.png</span> y <span class="code">e-status/web/static/img/CEIP_membrete.png</span>.</li>
			<li>La página "Acerca de", la cual incluye una animación y varias imágenes. La página está ubicada en <span class="code">e-status/web/WEB-INF/jsp/about.jsp</span></li>
		</ul>
	</body>
</html>
