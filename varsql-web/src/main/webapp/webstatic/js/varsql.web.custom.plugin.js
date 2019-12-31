/**
ytkim
varsql custom plugin ui js
 */
/* ========================================================================
 * Bootstrap: dropdown.js v3.3.6
 * http://getbootstrap.com/javascript/#dropdowns
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */
+function ($) {
  'use strict';

  // DROPDOWN CLASS DEFINITION
  // =========================

  var backdrop = '.dropdown-backdrop'
  var toggle   = '[data-toggle="dropdown"]'
  var Dropdown = function (element, option) {
	this._options = option;
	this.selectParent = '';
    $(element).on('click.bs.dropdown', this.toggle)
  }

  Dropdown.VERSION = '3.3.6'

  function getParent($this) {
    var selector = $this.attr('data-target')

    if (!selector) {
      selector = $this.attr('href')
      selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
    }

    var $parent = selector && $(selector);

    return $parent && $parent.length ? $parent : $this.parent();
   
    //console.log('$$$$$$ asdfasdf ',$this,selector,  reval);
     
  }

  Dropdown.prototype.clearMenus= function (e) {
    var _this = this;
    
    var tmptarget = e?e.target:'';
    if (e && e.which === 3) return
    
    var parentObj = $(tmptarget).closest('.dropdown-menu');
    
    $(backdrop).remove(); 
    $(toggle).each(function () {
      var $this         = $(this)
      var $parent       = getParent($this)
      var relatedTarget = { relatedTarget: this }
      
      if(parentObj.is($parent)) return ; 
      
      if (!$parent.hasClass('open')) return

      if (e && e.type == 'click' && /input|textarea/i.test(e.target.tagName) && $.contains($parent[0], e.target)) return

      $parent.trigger(e = $.Event('hide.bs.header-dropdown', relatedTarget))
      
      
      if (e.isDefaultPrevented()) return
	  
      $this.attr('aria-expanded', 'false').removeClass('on');
      
      $('.bs-dropdown-iframe-cover').hide();
      
      $parent.removeClass('open').trigger($.Event('hidden.bs.header-dropdown', relatedTarget))
    })
  }

  Dropdown.prototype.toggle = function (e) {
    var _this = this; 
    var $this = $(this)

    if ($this.is('.disabled, :disabled')) return

    var $parent  = getParent($this)
        
    var isActive = $parent.hasClass('open')
	var _drop_down_idx = $this.attr('_drop_down_idx'); 
	
	if(Dropdown.selectParent != $parent.selector || _drop_down_idx==$parent.attr('_drop_down_idx')){
		Dropdown.prototype.clearMenus();
	}
	Dropdown.selectParent = $parent.selector;

	$parent.attr('_drop_down_idx',$this.attr('_drop_down_idx'));

    if (!isActive) {
      if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
        // if mobile we use a backdrop because click events don't delegate
        $(document.createElement('div'))
          .addClass('dropdown-backdrop')
          .insertAfter($(this))
          .on('click', _this.clearMenus)
      }

      var relatedTarget = { relatedTarget: this }
      $parent.trigger(e = $.Event('show.bs.header-dropdown', relatedTarget))

      if (e.isDefaultPrevented()){
 		 if($this.attr('bgiframe')=='true') $($parent.find('.dropdown-menu')).bgiframe();
 		return
 	  }
      
      var bsDoropDown = $this.data('bs.header-dropdown'); 

	  if(bsDoropDown && bsDoropDown._options && $.isFunction(bsDoropDown._options.beforeClick)){
		  bsDoropDown._options.beforeClick($this);
	  }
	
      $this
        .trigger('focus')
        .attr('aria-expanded', 'true')
		.addClass('on');
      
      $(bsDoropDown && bsDoropDown._options.iframeCoverSelector).each(function (i, item){
		 var sItem = $(this);
		 var tmpNextEle = sItem.next();
		 if(!tmpNextEle.hasClass('bs-dropdown-iframe-cover')){
			 sItem.after('<div class="bs-dropdown-iframe-cover" style="position:absolute;"></div>');
			 tmpNextEle =sItem.next('.bs-dropdown-iframe-cover');
		 }else{
			 tmpNextEle.show();
		 }
		 
		 tmpNextEle
			.css('width',sItem.css('width')).css('height',sItem.height())
			.css('z-index',1).css('left',sItem.position().left)
			.css('top',sItem.position().top);
	 })
	 
      $parent
        .toggleClass('open')
        .trigger($.Event('shown.bs.header-dropdown', relatedTarget))
	
	  if($this.attr('bgiframe')=='true') $($parent.find('.dropdown-menu')).bgiframe();
    }

    return false
  }

  Dropdown.prototype.keydown = function (e) {
	  
    if (!/(38|40|27|32)/.test(e.which) || /input|textarea/i.test(e.target.tagName)) return

    var $this = $(this)

    e.preventDefault()
    e.stopPropagation()

    if ($this.is('.disabled, :disabled')) return

    var $parent  = getParent($this)
    var isActive = $parent.hasClass('open')

    if (!isActive && e.which != 27 || isActive && e.which == 27) {
      if (e.which == 27) $parent.find(toggle).trigger('focus')
      return $this.trigger('click')
    }

    var desc = ' li:not(.disabled):visible a'
    var $items = $parent.find('.dropdown-menu' + desc)

    if (!$items.length) return

    var index = $items.index(e.target)

    if (e.which == 38 && index > 0)                 index--         // up
    if (e.which == 40 && index < $items.length - 1) index++         // down
    if (!~index)                                    index = 0

    $items.eq(index).trigger('focus')
  }
  function getUUID(){
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random()*16)%16 | 0;
		d = Math.floor(d/16);
		return (c=='x' ? r : (r&0x7|0x8)).toString(16);
	});
	return uuid;
  }

  // DROPDOWN PLUGIN DEFINITION
  // ==========================

  function Plugin(option) {
	  
	 
	option = $.extend({
		opt:(typeof option === 'string')?option:false
		,bgiframe:true
		,iframeCoverSelector : '#gainPage'
		,clickClose:true
	}, option);
	
	return this.each(function (i,item) {
    	
      var $this = $(this)
      
      var data  = $this.data('bs.header-dropdown')
	  $this.attr('bgiframe',option.bgiframe);
	  $this.attr('_drop_down_idx',getUUID());
	  
	  if (!data) {
		  $this.data('bs.header-dropdown', (data = new Dropdown(this,option)))
		  data['_options'] = option;
		  
	  }
	  if (typeof option.opt == 'string') data[option.opt].call($this)
    })
  }

  var old = $.fn.dropdown

  $.fn.dropdown             = Plugin
  $.fn.dropdown.Constructor = Dropdown


  // DROPDOWN NO CONFLICT
  // ====================

  $.fn.dropdown.noConflict = function () {
    $.fn.dropdown = old
    return this
  }


  // APPLY TO STANDARD DROPDOWN ELEMENTS
  // ===================================

  $(document)
    .on('click.bs.dropdown.data-api', Dropdown.prototype.clearMenus)
    .on('click.bs.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() })
    .on('click.bs.dropdown.data-api', toggle, Dropdown.prototype.toggle)
    .on('keydown.bs.dropdown.data-api', toggle, Dropdown.prototype.keydown)
    .on('keydown.bs.dropdown.data-api', '.dropdown-menu', Dropdown.prototype.keydown)

}(jQuery);



/*! Copyright (c) 2013 Brandon Aaron (http://brandon.aaron.sh)
 * Licensed under the MIT License (LICENSE.txt).
 *
 * Version 3.0.1
 *
 * Requires jQuery >= 1.2.6
 */
(function (factory) {
    if ( typeof define === 'function' && define.amd ) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if ( typeof exports === 'object' ) {
        // Node/CommonJS style for Browserify
        module.exports = factory;
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {
    $.fn.bgiframe = function(s) {
        s = $.extend({
            top         : 'auto', // auto == borderTopWidth
            left        : 'auto', // auto == borderLeftWidth
            width       : 'auto', // auto == offsetWidth
            height      : 'auto', // auto == offsetHeight
            opacity     : true,
            src         : 'javascript:false;',
            conditional : true//(navigator.userAgent.toLowerCase().indexOf("msie") != -1) // expression or function. return false to prevent iframe insertion
        }, s);

        // wrap conditional in a function if it isn't already
        if ( !$.isFunction(s.conditional) ) {
            var condition = s.conditional;
            s.conditional = function() { return condition; };
        }
        
        var $iframe = $('<iframe class="bgiframe"frameborder="0"tabindex="-1"src="'+s.src+'"style="display:block;position:absolute;z-index:-1;"/>');

        return this.each(function() {
            var $this = $(this);
            if ( !s.conditional(this)) { return; }
            var existing = $this.children('iframe.bgiframe');
            var $el = existing.length === 0 ? $iframe.clone() : existing;
            $el.css({
                'top': s.top == 'auto' ?
                    ((parseInt($this.css('borderTopWidth'),10)||0)*-1)+'px' : prop(s.top),
                'left': s.left == 'auto' ?
                    ((parseInt($this.css('borderLeftWidth'),10)||0)*-1)+'px' : prop(s.left),
                'width': s.width == 'auto' ? (this.offsetWidth + 'px') : prop(s.width),
                'height': s.height == 'auto' ? (this.offsetHeight + 'px') : prop(s.height),
                'opacity': s.opacity === true ? 0 : undefined
            });

            if ( existing.length < 1 ) {
                $this.prepend($el);
            }
        });
    };

    // old alias
    $.fn.bgIframe = $.fn.bgiframe;

    function prop(n) {
        return n && n.constructor === Number ? n + 'px' : n;
    }

}));
