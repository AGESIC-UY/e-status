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
jQuery(document).ready(function() {
	jQuery.timeago.settings.strings = {
		prefixAgo: null,
		prefixFromNow: null,
		suffixAgo: '',
		suffixFromNow: '',
		seconds: '1m',
		minute: '1m',
		minutes: '%dm',
		hour: '1h',
		hours: '%dh',
		day: '1d',
		days: '%dd',
		month: '1me',
		months: '%dme',
		year: '1a',
		years: '%da',
		wordSeparator: ' ',
		numbers: []
	};
	jQuery("time.timeago").timeago();
	var interval_id = window.setInterval(reload, parseInt(jQuery('input#ajax_period').val()));
	jQuery('input#interval_id').val(interval_id);
	//Functions to expand/shrink pods
	jQuery('div.pod').on('mouseover', function(event) {
		if (!jQuery(this).hasClass('expanded')) {
			jQuery(this).addClass('expanded');
		}
	});
	jQuery('div.pod').on('mouseout', function(event) {
		if (!jQuery.contains(this, event.relatedTarget) && this != event.relatedTarget) {
			jQuery(this).removeClass('expanded');
		}
	});
});
function reload() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var xmlDoc = xmlhttp.responseXML;
			var indicators = xmlDoc.getElementsByTagName("indicator");
			for (var i = 0; i < indicators.length; i++) {
				var row = jQuery('#' + indicators[i].getElementsByTagName('id')[0].textContent);
				if (row != null) {
					var name = indicators[i].getElementsByTagName('name')[0].textContent;
					var val = indicators[i].getElementsByTagName('val')[0].textContent;
					var taken = indicators[i].getElementsByTagName('taken')[0].textContent;
					var tendency = indicators[i].getElementsByTagName('tendency')[0].textContent;
					var classification = indicators[i].getElementsByTagName('classification')[0].textContent;
					jQuery('.name > a', row).html(name);
					var oldVal = jQuery('span.val', row).html();
					jQuery('span.val', row).html(val);
					if (oldVal != val) {
						playSumEffect(row, oldVal);
					}
					if (tendency < 0) {
						jQuery('.arrow', row).html('▼');
					} else if (tendency > 0) {
						jQuery('.arrow', row).html('▲');
					} else {
						jQuery('.arrow', row).html('=');
					}
					jQuery('time', row).timeago('update', taken);
					jQuery('.tag', row).removeClass().addClass('tag').addClass('c' + classification);
				}
			}
		} else if (xmlhttp.readyState == 4 && xmlhttp.status != 200) {
			window.clearInterval(parseInt(jQuery('input#interval_id').val()));
			jQuery('.pod').remove();
			jQuery('#exception-container').show();
		}
	}
	xmlhttp.open('GET', 'recargar', true);
	xmlhttp.send();
}