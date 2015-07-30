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
	hideInputs();
	jQuery('button#web').on('click', function() {
		jQuery('form').attr('target', '_self');
	});
	jQuery('button#file').on('click', function() {
		jQuery('form').attr('target', '_blank');
	});
	if (jQuery('#datatable').length != 0) {
		makeChart();
	}
	if(typeof(Storage) !== "undefined") {
		var selectedIndex = localStorage.getItem(FORMAT_KEY);
		if (selectedIndex == '') {
			localStorage.setItem(FORMAT_KEY, '0');
		} else {
			jQuery('select#formats')[0].selectedIndex = parseInt(selectedIndex);
			changeFormat(jQuery('select#formats')[0]);
		}
	}
});
function hideDays(monthSelector) {
	var d = jQuery('#' + monthSelector).val();
	var daySelectorId;
	if (monthSelector == 'monthfrom') {
		daySelectorId = 'dayfrom';
	} else if (monthSelector == 'monthto') {
		daySelectorId = 'dayto';
	}
	jQuery('#' + daySelectorId + '> option').show();
	if (d == 4 || d == 6 || d == 9 || d == 11 || d == 2) {
		jQuery('#' + daySelectorId + '> option[value=31]').hide();
		if (d == 2) {
			jQuery('#' + daySelectorId + '> option[value=30], #' + daySelectorId + '> option[value=29]').hide();
		}
	}
	var newIndex = jQuery('#' + daySelectorId)[0].selectedIndex;
	//If the selected day is now hidden, select a previous one
	while (jQuery('#' + daySelectorId + '> option:nth(' + newIndex + ')').css('display') == 'none') {
		newIndex--;
	}
	jQuery('#' + daySelectorId)[0].selectedIndex = newIndex;
}
function hideGroupByInputs() {
	if (jQuery('#groupby').val() == 0) {
		jQuery('label[for=groupusing], #groupusing').css('visibility', 'hidden');
		jQuery('#year')[0].selectedIndex = 0;
		jQuery('#year').removeAttr('multiple');
	} else {
		jQuery('label[for=groupusing], #groupusing').css('visibility', 'visible');
		jQuery('#year').attr('multiple', 'multiple');
	}
}
function hideDayInputs() {
	if (jQuery('#groupby').val() == 2) {
		jQuery('#dayfrom, #dayto, label[for=dayfrom], label[for=dayto]').css('visibility', 'hidden');
	} else {
		jQuery('#dayfrom, #dayto, label[for=dayfrom], label[for=dayto]').css('visibility', 'visible');
	}
}
function hideInputs() {
	hideDays('monthfrom');
	hideDays('monthto');
	hideGroupByInputs();
	hideDayInputs();
}