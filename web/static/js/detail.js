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
	jQuery('input.fh').on('change', function() {
		if (this.checked) {
			jQuery('.filter', this.parentElement.parentElement).removeAttr('disabled');
		} else {
			jQuery('.filter', this.parentElement.parentElement).attr('disabled', 'disabled');
		}
	});
	jQuery('button#web').on('click', function() {
		if (jQuery('[required]').not('[disabled]').filter(function(index, element) {return element.value == '';}).length == 0) {
			if ((jQuery('#file-download-warning-used').length > 0 || confirm(jQuery('input#file-download-warning').val()))) {
				jQuery('form').attr('target', '_self');
				block();
			} else {
				return false;
			}
		} else {
			jQuery('form').attr('target', '_self');
		}
	});
	jQuery('button#file').on('click', function() {
		jQuery('form').attr('target', '_blank');
	});
	jQuery(window).on('pagehide', function() {
		jQuery('div#block').hide();
	});
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