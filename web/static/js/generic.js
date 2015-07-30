/*
 *  Copyright 2015 Consejo de Educación Inicial y Primaria - Administración Nacional de Educación Pública - Uruguay
 *
 *  This file is part of e-status.
 *
 *  e-status is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  e-status is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with e-status.  If not, see <http://www.gnu.org/licenses/>.
 */
var FORMAT_KEY = 'format';

jQuery(document).ready(function() {
	startTime();
	jQuery('input.datepicker').datepicker({
	    dateFormat: "dd/mm/yy",
	    constrainInput: true,
	    changeMonth: true,
	    changeYear: true
	});
});
function startTime() {
	var today = new Date();
	var y = today.getFullYear();
	var M = today.getMonth() + 1;
	var d = today.getDate();
	var h = today.getHours();
	var m = today.getMinutes();
	M = formatTime(M);
	d = formatTime(d);
	m = formatTime(m);
	jQuery('#clock').html(d + '/' + M + '/' + y + ' ' + h + ':' + m);
	var t = setTimeout(function(){startTime();}, 1000 * 5);
}
function formatTime(i) {
	if (i < 10) {
		i = '0' + i;   // add zero in front of numbers < 10
	}
	return i;
}
function block() {
	var opts = {
		lines: 17, // The number of lines to draw
		length: 11, // The length of each line
		width: 3, // The line thickness
		radius: 18, // The radius of the inner circle
		corners: 0, // Corner roundness (0..1)
		rotate: 17, // The rotation offset
		direction: 1, // 1: clockwise, -1: counterclockwise
		color: '#FFFFFF', // #rgb or #rrggbb or array of colors
		speed: 1, // Rounds per second
		trail: 100, // Afterglow percentage
		shadow: false, // Whether to render a shadow
		hwaccel: true, // Whether to use hardware acceleration
		className: 'spinner', // The CSS class to assign to the spinner
		zIndex: 2e9, // The z-index (defaults to 2000000000)
		top: '50%', // Top position relative to parent
		left: '50%' // Left position relative to parent
	};
	jQuery('body').append('<div id="block"><p>Ejecutando consulta. Esto puede demorar unos minutos.</p></div>');
	var block = jQuery('div#block');
	var warn = function() {
		jQuery('div#block > p').html('Aún en progreso...');
	}
	var spinner = new Spinner(opts).spin(block[0]);
	block.show();
	window.setTimeout(warn, 10000);
}

function playSumEffect(indicator, oldVal) {
	var diff = (jQuery('span.val', indicator).html().replace(/\.|%/g, '').replace(/,/g, '.') - oldVal.replace(/\.|%/g, '').replace(/,/g, '.'));
	if (jQuery('span.val', indicator).html().indexOf(',') != -1) {
		diff = diff.toFixed(2);
	}
	var newSumElement = jQuery('.val.sum', indicator).clone(true);
	jQuery('.val.sum', indicator).remove();
	if (diff > 0) {
		diff = '+' + diff;
	}
	diff = diff.toLocaleString("es-UY");
	if (oldVal.indexOf('%') != -1) {
		diff = diff + '%';
	}
	newSumElement.html(diff);
	jQuery('div.val', indicator).after(newSumElement);
}

function makeChart() {
	var chart_types = {
		CATEGORIES: {name: 'Categorías', type: 'category'},
		DATE: {name: 'Días', type: 'datetime'},
		DATETIME: {name: 'Día/Hora', type: 'datetime'}
	};
	var series = [];
	var categories = [];
	var $data_table = $("#datatable");
	if (!$data_table)
		return;

	//label para eje X
	var x_axis_label = $data_table.find("thead th:eq(0)").text();

	//primer <tr>, primer <td>, 
	var x_axis_data = $data_table.find("tbody tr:first td:first span.hidden").text();

	var chart_type;

	if (x_axis_data.split(/[-: \.]/).length === 7) {
		chart_type = chart_types.DATETIME;
	} else if (x_axis_data.split(/[- :\.]/).length === 3) {
		chart_type = chart_types.DATE;
	} else {
		chart_type = chart_types.CATEGORIES;
	}

	//buscar todos los TH menos el primero (nombre el eje X)
	//los restantes son los nombres de las series
	$data_table.find("thead tr:first th:gt(0)").each(function(i, th) {
		series[i] = {
			name: $(th).text(),
			data: []
		};
	});

	$data_table.find("tbody tr").each(function(i, tr) {
		var date;
		//si la grafica es de fechas, obtenemos la fecha que corresponde a esta fila
		//cada columna representaria un año, pero solo nos importa mes/año
		if (chart_type === chart_types.DATE || chart_type === chart_types.DATETIME) {
			var aux = $(tr).find('td:first span.hidden').text(); //texto del primer <td> es la fecha
			var parts = aux.split(/[-: \.]/);

			//le resto 1 al mes porque asi los representa javascript (los meses desde 0)
			date = Date.UTC(1991, parseInt(parts[1] - 1, 10), parseInt(parts[2], 10), parseInt(parts[3], 10) || 0, parseInt(parts[4], 10) || 0, parseInt(parts[5], 10) || 0);
		}
		$(tr).find('td').each(function(j, td) {
			if (j === 0) {
				categories.push($('span.visible', td).text());
			} else {
				var text = $('span.hidden', td).text();
                                var parsed = parseFloat(text);
                                var value = isNaN(parsed)? null: parsed; //si es valor vacio tenemos que poner null, no podemos hacer un if(parsed) porque el valor 0 no podria ponerse
				if (chart_type === chart_types.CATEGORIES) { //sino (habiamos decidido que es de categorias)
					series[j - 1].data.push(value);
				} else { //si decidimos que esta grafica es de fechas
					series[j - 1].data.push([date, value]);
				}
			}
		});

	});

	//Grafica
	Highcharts.setOptions({
		lang: {
			loading: 'Cargando...',
			months: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
			weekdays: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
			shortMonths: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
			exportButtonTitle: "Exportar",
			printButtonTitle: "Importar",
			rangeSelectorFrom: "De",
			rangeSelectorTo: "A",
			rangeSelectorZoom: "Periodo",
			downloadPNG: 'Descargar gráfica PNG',
			downloadJPEG: 'Descargar gráfica JPEG',
			downloadPDF: 'Descargar gráfica PDF',
			downloadSVG: 'Descargar gráfica SVG',
			printChart: 'Imprimir Gráfica',
			thousandsSep: ".",
			decimalPoint: ',',
			contextButtonTitle: 'Menú de gráfica'
		}
	});
	$('#chart').highcharts({
		title: {
			text: '_',
			x: -20 //center
		},
		subtitle: {
			text: '',
			x: -20
		},
		xAxis: {
			categories: chart_type === chart_types.CATEGORIES ? categories : null,
			type: chart_type.type,
			dateTimeLabelFormats: {// don't display the dummy year
				month: '%e. %b',
				year: '%b'
			},
			title: {
				text: x_axis_label
			}
		},
		yAxis: {
			title: {
				text: ''
			},
			plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}],
			labels: {
				formatter: function() {
					if (jQuery('input#val_is_percentage').val() === 'true') {
						return this.value + '%';
					} else {
						return this.value;
					}
				}
			}
		},
		tooltip: {
			headerFormat: '<b>{series.name}</b><br>',
			pointFormat: chart_type === chart_types.CATEGORIES ? '{point.category}: {point.y}' : '{point.x:%e. %b}: {point.y}',
			valueDecimals: 2
		},
		legend: {
			layout: 'vertical',
			align: 'left',
			verticalAlign: 'middle',
			borderWidth: 0
		},
		series: series
	});
	jQuery('input#min').val(jQuery('#chart').highcharts().yAxis[0].min);
	jQuery('input#max').val(jQuery('#chart').highcharts().yAxis[0].max);
}
function updateChart() {
	var options = jQuery('#chart').highcharts().options;
	options.yAxis[0].min = jQuery('input#min').val();
	options.yAxis[0].max = jQuery('input#max').val();
	jQuery('#chart').highcharts(options);
}

function changeFormat(select) {
	jQuery('button#file').val(
		jQuery('option', select)[jQuery(select)[0].selectedIndex].value
	);
	if(typeof(Storage) !== "undefined") {
		localStorage.setItem(FORMAT_KEY, jQuery(select)[0].selectedIndex + '');
	}
}