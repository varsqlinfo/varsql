;(function ($, window, document, undefined) {

    var pluginName = "contextMenu"
		,initialized = false
        ,defaults = {
			fadeSpeed: 100			
			,filter: function ($obj) {
				// Modify $obj, Do not return
			}
			,above: 'auto'
			,preventDoubleContext: true
			,compress: true
			,selectCls : 'item_select'
			,callback:function (key){
				alert(key)
			}
			,beforeSelect :function (item){
				
			}
		};
        
    function Plugin(element, options) {
        this.selector = element;
		this.contextId = 'dropdown-'+new Date().getTime();
        this.options = $.extend({}, defaults, options);
		this.contextData = {};
		this.selectElement = $('');
		if(initialized===false){
			this.init(); 
			initialized = true; 
		}
		this.addContext();
		this.context = $('#'+this.contextId);

		return element; 
    }

	Plugin.prototype ={
		init :function(){
			defaults = this.options;
			
			$(document).on('mousedown', 'html', function (e) {
				if(e.which !==2 && $(e.target).closest('.dropdown-context').length < 1){
					$('.dropdown-context').fadeOut(defaults.fadeSpeed, function(){
						$('.dropdown-context').css({display:''}).find('.drop-left').removeClass('drop-left');
					});
				}
			});

			if(defaults.preventDoubleContext){
				$(document).on('contextmenu', '.dropdown-context', function (e) {
					e.preventDefault();
				});
			}

			$(document).on('mouseenter', '.context-submenu', function(){
				var $sub = $(this).find('.dropdown-context-sub:first'),
					subWidth = $sub.width(),
					subLeft = $sub.offset().left,
					collision = (subWidth+subLeft) > window.innerWidth;
				if(collision){
					$sub.addClass('drop-left');
				}
			});
		}
		,updatedefaults:function (opts){
			defaults = $.extend({}, defaults, opts);
		}
		,buildMenu : function (data, id, subMenu, depth){
			var _self = this; 
			var subClass = (subMenu) ? ' dropdown-context-sub' : '',
				compressed = defaults.compress ? ' compressed-context' : '',
				$menuHtm = [];
			
			$menuHtm.push('<ul class="context-menu dropdown-context' + subClass + compressed+'" id="' + id + '">');
	 
			var item ,linkTarget = '',itemKey;;
			for(var i = 0; i<data.length; i++) {
				item = data[i]; 
				
				if (typeof item.divider !== 'undefined') {
					$menuHtm.push('<li class="divider" context-key="divider"></li>');
					continue;
				}
				
				if (typeof item.header !== 'undefined') {
					$menuHtm.push('<li class="nav-header" context-key="header">' + item.header + '</li>');
					continue; 
				}
				
				itemKey = depth+'_'+item.key; 
				_self.contextData[itemKey] = item;
			
				if (typeof item.target !== 'undefined') {
					linkTarget = ' target="'+item.target+'"';
				}

				if (typeof item.subMenu !== 'undefined') {
					$menuHtm.push('<li class="context-submenu ui-context-item" context-key="'+itemKey+'"><a tabindex="-1">' + item.name + '</a>');
				} else {
					$menuHtm.push('<li class="ui-context-item" context-key="'+itemKey+'"><a tabindex="-1">' + item.name + '</a>');
				}

				if (typeof item.subMenu != 'undefined') {
					var subMenuData = _self.buildMenu(item.subMenu, id, true,depth+1);
					$menuHtm.push(subMenuData);
				}
				
				$menuHtm.push('</li>');
			}
			$menuHtm.push('</ul>');

			return $menuHtm.join('');
		}
		,closeContextMenu : function (){
			this.context.hide();
		}
		,destory:function (){
			$(document).off('contextmenu', this.element).off('click', '.context-event');
		}
		/**
		*
		* 컨텍스트 메뉴 이벤트 처리. 
		*/
		,contextEvent : function (){
			var _self = this,opt = _self.options;
			
			$('#'+_self.contextId+' .ui-context-item').on('click',function (){
				var clickEle=$(this);
				
				if(clickEle.hasClass('context-submenu')){
					
				}else{
					skey = clickEle.attr('context-key');
					var sobj = {
						key : skey
						,item : _self.contextData[skey]
						,list : _self.contextData
						,element : _self.selectElement
					}
				
					if(jQuery.isFunction(opt.callback)){
						opt.callback.call(sobj, sobj.item.key, sobj.item);
					}else{
						alert(skey);
					}
					_self.closeContextMenu();
				}
			});
		}
		,addContext : function (){
			var _self = this; 
			var id = _self.contextId
				,opt = _self.options
				,$menu =_self.buildMenu(opt.items, id, false, 0)
				,selector = _self.selector;
				
			$menu = '<div id="'+id+'_wrap" onselectstart="return false" draggable="false">'+$menu+'</div>';
			
			$('body').append($menu);
			
			_self.contextEvent();

			$(document).off('contextmenu', _self.selector);

			$(document).on('contextmenu', _self.selector, function (e) {
				e.preventDefault();
				e.stopPropagation();
				
				//  이전 선택한 클래스 삭제 . 
				_self.selectElement.removeClass(opt.selectCls);

				var clickObj = $(this);
				clickObj.addClass(opt.selectCls);
				_self.selectElement = clickObj; 

				var $dd = $('#'+ id);
				
				if(jQuery.isFunction(opt.beforeSelect))	{
					opt.beforeSelect.call(this);
				}

				var eleH = $dd.height()
					,evtX = e.pageX
					,evtY=e.pageY;
				
				console.log('eleH : ' + eleH,'evtX : ' + evtX,'evtY : ' + evtY,  $(document).height());

				if ( evtY > eleH+5 && (evtY + eleH+5) > $(document).height()) {
					$dd.css({
						top: evtY - (eleH+5),
						left: evtX +5
					}).fadeIn(defaults.fadeSpeed);
				} else {
					$dd.css({
						top: evtY + 5,
						left: evtX + 5
					}).fadeIn(defaults.fadeSpeed);
				}
			});
		}
	};

    $[ pluginName ] = function (selector,options) {
		var option = $.extend({
			opt:(typeof options === 'string')?option:false
			,bgiframe:true
		}, options);

		return new Plugin(selector, option);
    };

})(jQuery, window, document);
