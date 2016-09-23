/*
**
*ytkim
*varsql.plugin.ui.js js
 */

;(function($, window, document, VARSQL) {
"use strict";

var _pluginUI=VARSQL.pluginUI||{};

_pluginUI.chart = {
	_chartInfo :{}
	// barchart 그리기
	,bar :function (selector , opt){
		var _this = this; 
		
		
		if(_this._chartEnable(selector , opt)) return ; 
		
		//$(selector).empty();
		
		 jui.ready([ "chart.builder" ], function(chart) {
	        _this._chartInfo[selector] = chart(selector, $.extend({},{
	        	 theme : "gradient"
	        },opt)); 
		})
	}
	// pie chart 그리기
	,pie :function (selector , opt){
		var _this = this; 
		
		$(selector).empty();
		
		//if(_this._chartEnable(selector , opt)) return ; 
		
		jui.ready([ "chart.builder" ], function(chart) {
	        chart(selector, $.extend({},{
	        	//theme : "jennifer"
	        },opt)); 
		})
	}
	,_chartEnable : function (selector , opt){
		var _this = this; 
		
		if(_this._chartInfo[selector]){
			_this._chartInfo[selector].axis(0).update(opt.axis[0].data);
			return true; 
		}
		
		return false; 
	}
	
}

VARSQL.pluginUI = _pluginUI;
}(jQuery, window, document,VARSQL));