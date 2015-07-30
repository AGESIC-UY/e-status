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
	jQuery('[class$="-hover"]').on('mouseover', function(event) {
		var prefix = jQuery(this).attr('class').split('-')[0];
		jQuery('.' + prefix + '-highlight').addClass('highlight');
	});
	jQuery('[class$="-hover"]').on('mouseout', function(event) {
		var prefix = jQuery(this).attr('class').split('-')[0];
		jQuery('.' + prefix + '-highlight').removeClass('highlight');
	});
	jQuery('.effect.hover').on('mouseover', function(event) {
		var val = parseInt(jQuery('span.val').html()) + (Math.random() < 0.5 ? -1 : 1) * parseInt(Math.random() * 50);
		var row = jQuery('.pod');
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

	});
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
	makeChart();
	jQuery('input.fh').on('change', function() {
		if (this.checked) {
			jQuery('.filter', this.parentElement.parentElement).removeAttr('disabled');
		} else {
			jQuery('.filter', this.parentElement.parentElement).attr('disabled', 'disabled');
		}
	});
});