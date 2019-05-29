/**
 * PubEPUI v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
 */ 
if (typeof window != "undefined") {
    if (typeof window.PubEPUI == "undefined") {
        window.PubEPUI = {};
    }
}else{
	if(!PubEPUI){
		PubEPUI = {};
	}
}

(function( window, $, PubEP) {

var _$base = {},
_defaultOption ={
	version:'0.1.0'
	,author:'ytkim'
}
,globalUIOption ={
	dialogBgIframe : true
	,dialog:{
		targetID : '_main_div_dialog_frame_id_'
		,title : '설정'
		,width : '480'
		,height : '355'
		,scrolling : 'no'
		,directCall : true
		,useScrollHidden : true
		,modal: true
		, autoOpen : false
		, resizable : false
		, draggable: true
		, titleClass : ''
	}
	,modal:{
		cancleBtn :'Cancle'
		,okBtn:'OK'
		,width:'300px'
		,useCancle : false
		, height:'auto'
		, title :'Confirm'
	}
	,toast : {
		hideAfter: 2000
		, position: {left:"50%",top:"50%"}
		, textColor: '#fff'
		, stack:false
		, showHideTransition: 'fade'
		, position: 'mid-center'
		, loader :false
		//, beforeShow : function(){$('.jq-toast-wrap').css("margin" , "0 0 0 -155px") }
	}
};

_$base.init = function (initOpt){
	globalUIOption=PubEP.util.objectMerge(globalUIOption,initOpt);
}

_$base.replaceHtm={
	title :function(option){
		
		if(!option){
			return '';
		}
		
		if(typeof option === 'string'){
			return escape(option);
		}else{
			$(option.selector).each(function (i,item){
				var sObj = $(item); 
				sObj.attr('title',unescape(sObj.attr('title')));
			})
		}
	}
}

/**
 * @method PubEP
 * @description dialog
 */
_$base.dialog={
	close : function (selector, parentWinChk){
		this.closeDialog(selector, parentWinChk);
	}
	,closeDialog : function (selector, parentWinChk){
		var _opener = window;
		
		if(parentWinChk !== false){
			if(typeof top.PubEPUI !=='undefined'){
				_opener = top; 
			}else if(typeof parent.PubEPUI !=='undefined'){
				_opener = parent; 
			}
		}
		
		var dialogEle;
		if(selector){
			dialogEle = _opener.$(selector);
		}else{
			dialogEle= _opener.$('#_main_div_dialog_frame_id_');
		}
		
		dialogEle.closest('.ui-dialog.ui-widget').find('.ui-dialog-titlebar .ui-dialog-titlebar-close').trigger('click');
		
		var htmlEle = $('html').get(0); 
		var isScroll = (htmlEle.scrollHeight > window.innerHeight)||(htmlEle.scrollWidth > window.innerWidth) || $('html').css('overflow-y') == 'scroll';
		
		isScroll = _opener.$('.pub-ep-ui-overlay').length > 0 ? false :isScroll; 
		
		if(isScroll){
			_opener.$('html').css('overflow','');
			_opener.$('body').css('overflow-y','');
		}
	}
	,html : function (selector , opt){
		this._dialog('html',selector ,opt);
	}
	,text : function (text , opt){
		this._dialog('text',text ,opt);
	}
	/**
	 * @method _$base.dialog.mgmtDialog
	 * @param _url {String} dialog url
	 * @param opt {Object} 상세 object
	 * @description 게시판 더보기
	 */	
	,frame :function (_url , opt){
		this._dialog('frame',_url ,opt);
	}
	,frameHtml : function (html ,opt){
		this._dialog('frameHtml',html ,opt);
	}
	,_dialog : function (mode , dialogInfo , opt){
		var _opener = window;
		
		if(opt.parentCheck !== false){
			if(typeof top.PubEPUI !=='undefined'){
				_opener = top; 
			}else if(typeof parent.PubEPUI !=='undefined'){
				_opener = parent; 
			}
		}
		
		opt.height = opt.height+'';
		var options = PubEP.util.objectMerge({}, globalUIOption.dialog ,opt);
		
		var _targetId = options.targetID; 
		
		if(_opener.$('#'+_targetId).length < 1){
			_opener.$('body').append('<div id="'+_targetId+'"></div>');
		}
		
		if(options.scrolling=='no'){
			$('#'+_targetId).css('overflow', 'hidden');
		}else{
			$('#'+_targetId).css('overflow', 'auto');
		}
		
		var htmlEle = $('html').get(0); 
		var isScroll = (htmlEle.scrollHeight > window.innerHeight)||(htmlEle.scrollWidth > window.innerWidth) || $('html').css('overflow-y') == 'scroll';
		
		isScroll = _opener.$('.pub-ep-ui-overlay').length > 0 ? false :isScroll;
		
		var modalOption =PubEP.util.objectMerge({},options);
		
		modalOption.close = function (event, ui){
			if(opt.closeOverflowAuto !==false){
				if(isScroll){
					if(options.useScrollHidden !== false){
						_opener.$('html').css('overflow','');
						_opener.$('body').css('overflow-y','');
					}
				}
			}
		}
		
		if(typeof options.position ==='undefined' && options.onlyCenter===true){
			modalOption.position= { my: "center center", at: "center center", of: window }
		}
		
		if(isScroll){
			if(options.useScrollHidden !== false){
				//position: fixed;
				_opener.$('html').css('overflow','hidden');
				_opener.$('body').css('overflow-y','scroll');
			}
		}
		
		modalOption.width = (modalOption.width+'').replace('px','');
		modalOption.height = (modalOption.height+'').replace('px','');
				
		var dialogEle;
		if(mode == 'html'){
			dialogEle =_opener.$(dialogInfo);
			
			dialogEle.dialog(modalOption)
			.dialog("open").parent().find('.ui-dialog-title').html('<h1 class="tit">'+options.title+'</h1>');
		}else{
			var dialogHtm;
			
			if(_opener.$('#'+_targetId+'iframe').length > 0){
				_opener.$('#'+_targetId).dialog("close");
			}
			
			if(mode=='frame'){
				if(options.directCall===false){
					dialogInfo=PubEP.getContextPath(dialogInfo);
				}
				modalOption.width = 'auto';
				modalOption.height = 'auto';
				modalOption.open =function(){
					_opener.PubEP.page.view(dialogInfo, "iframe", $.extend({},{target:"#"+_targetId+'iframe', gubun:"dialog", gubunkey:"portletTabConfig",name:decodeURIComponent("게시판 목록 관리")},opt));
				}
				dialogHtm = '<iframe id="'+_targetId+'iframe" name="'+_targetId+'iframe" src="" style="width:'+options.width.replace('px','')+'px;height:'+options.height.replace('px','')+'px" frameborder="0" scrolling="'+options.scrolling+'"></iframe>'; 
			}else if(mode=='frameHtml'){
				modalOption.width = 'auto';
				modalOption.height = 'auto';
				modalOption.open =function(){
					var previewFrame = document.getElementById(_targetId+'iframe');
			        var preview =  previewFrame.contentDocument ||  previewFrame.contentWindow.document;
			        preview.open();
			        preview.write(dialogInfo);
			        preview.close();
				}
				dialogHtm = '<iframe id="'+_targetId+'iframe" name="'+_targetId+'iframe" src="" style="width:'+options.width+'px;height:'+options.height+'px" frameborder="0" scrolling="'+options.scrolling+'"></iframe>';
			}else if(mode=='text'){
				modalOption.width = options.width;
				modalOption.height =options.height;
				modalOption.open = function (){};
				dialogHtm = dialogInfo;
			}
			
			dialogEle = _opener.$('#'+_targetId);
			dialogEle.html(dialogHtm)
			.dialog(modalOption)
			.dialog("open").parent().find('.ui-dialog-title').html('<h1 class="tit">'+options.title+'</h1>');
		}
		
		if(options.cssStyle!=''){
			dialogEle.attr('style',options.cssStyle);
		}
		
		var uiDiloagWidgetEle = dialogEle.closest('.ui-dialog.ui-widget');
		
		if(options.titleClass != ''){
			uiDiloagWidgetEle.find('.ui-dialog-titlebar').addClass(options.titleClass);
		}
		
		if(options.titleHide===true){
			uiDiloagWidgetEle.find('.ui-dialog-titlebar').hide();
		}else{
			uiDiloagWidgetEle.find('.ui-dialog-titlebar').show();
			//_opener.$('.ui-widget-overlay.ui-front').css('height',_opener.$(_opener.document).height());
		}
		
		var _uuid = 'bgiframe-'+PubEP.util.generateUUID();
		
		var overLayEle = uiDiloagWidgetEle.nextAll('.ui-widget-overlay.ui-front:not(.pub-ep-ui-overlay)'); 
		overLayEle.addClass('pub-ep-ui-overlay');
		overLayEle.append('<div class="bg-iframe-overlay '+_uuid+'" style="display:block;position:fixed;z-index:1;top:0px;left:0px;width:100%;height:100%;opacity:0;"></div>')
		
		if(options.bgiframe !== false && globalUIOption.dialogBgIframe !== false){
			overLayEle.bgiframe();
		}
		
		if(options.overlayHide !== false){
			_opener.$('.'+_uuid).on('click' , function (){
				if(isScroll){
					if(options.useScrollHidden !== false){
						_opener.$('html').css('overflow','');
						_opener.$('body').css('overflow-y','');
					}
				}
				dialogEle.dialog("close");
			})
		}
		
		return dialogEle; 
	}
}

_$base.modal = {
	modalEle : false
	,template : function (opt){
		var strHtm = [];
		strHtm.push('<dl class="pubep-modal" style="width:#width#;height:#height#;">');
		strHtm.push('	<dt class="pubep-modal-header">#title#</dt>');
		strHtm.push('	<dd class="pubep-modal-body">');
		strHtm.push('		#message#</dd>');
		strHtm.push('	<dd class="pubep-modal-footer">');
		
		if(opt.useCancle !== false){
			strHtm.push('		<a href="javascript:void(0);" class="pubep-modal-btn white pubEpModalCancle">');
			strHtm.push('			#cancleBtn#');
			strHtm.push('		</a>');
		}
		
		if(opt.useOk !== false){
			strHtm.push('		<a href="javascript:void(0);" class="pubep-modal-btn black pubEpModalOk">');
			strHtm.push('			#okBtn#');
			strHtm.push('		</a>');
		}

		strHtm.push('	</dd>');
		strHtm.push('</dl>');
		
		return strHtm.join('');
	}
	/* opt {
		message : '메시지'
		width:  넓이
		height : 높이 
		cancleCallback : 취소 메소드 또는 string
		okCallback :  ok 메소드 또는 string
		cancleBtn : cancle 문구
		okBtn : ok 문구
		
	}
	*/
	,open : function (opt){
		
		var _opener = window;
		var isFrame = false; 
		
		if(typeof top.PubEPUI !=='undefined' && top != window){
			_opener = top; 
			isFrame =true;
		}else if(typeof parent.PubEPUI !=='undefined'){
			_opener = parent; 
			isFrame =true;
		}
		
		opt.isFrame = isFrame;
		_opener.PubEPUI.modal._open(opt);
	}
	,close : function(){
		var _opener = getOpenWin();
		//console.log(_opener.location.href);
		
		_opener.PubEPUI.dialog.closeDialog('#pubEpUiModalArea', false);
	}
	,getElementVal : function (selector){
		return getOpenWin().$(selector).val();
	}
	,_open : function (opt){
		var _this = this; 
		
		opt = PubEP.util.objectMerge({},globalUIOption.modal,opt);
		
		var confirmHtml = PubEP.util.replaceParam(_this.template(opt), opt);
		
		if($('#pubEpUiModalArea').length < 1){
			$('body').append('<div id="pubEpUiModalArea" class="pubep-modal-wrapper"></div>');
			
			_this.modalEle = $('#pubEpUiModalArea');
		}
		
		_this.modalEle.off('click.pubepui.modal');
				
		_this.modalEle.on("click.pubepui.modal",'.pubEpModalCancle,.btn_close',function (){
			PubEPUI.dialog.closeDialog('#pubEpUiModalArea', false);
			
			if(PubEP.isUndefined(opt.cancleCallback)) return ; 
			
			if(PubEP.isFunction(opt.cancleCallback)){
				opt.cancleCallback();
			}else{
				if(opt.isFrame){
					$('#epContentViewFrame').get(0).contentWindow[opt.cancleCallback]();
				}else{
					window[opt.cancleCallback]();
				}
			}
		});
		
		_this.modalEle.on("click.pubepui.modal",'.pubEpModalOk', function (){
			
			if(opt.autoClose !==false ){
				PubEPUI.dialog.closeDialog('#pubEpUiModalArea',false);
			}
			
			if(PubEP.isUndefined(opt.okCallback)) return ; 
			
			if(PubEP.isFunction(opt.okCallback)){
				opt.okCallback();
			}else{
				if(opt.isFrame){
					$('#epContentViewFrame').get(0).contentWindow[opt.okCallback]();
				}else{
					window[opt.okCallback]();
				}
			}
		})
		
		_this.modalEle.empty().html(confirmHtml);
		
		//console.log({overlayHide :false ,parentCheck :false , width:(opt.width||'auto'),height:(opt.height||'auto'),titleHide:true});
		
		return _$base.dialog.html('#pubEpUiModalArea',{overlayHide :false ,parentCheck :false , width:(opt.width||'auto'),height:'auto', onlyCenter :true,titleHide:true});
	}
}

_$base.pageCountList = function (selector ,opt){
	
	if(typeof opt === 'undefined'){
		return $(selector+' .countPerPage').val();
	}
	
	var opt = $.extend({},{count:[10,50,100]},opt);
	
	var fnChange = opt.change
		,countArr = opt.count; 
	
	var strHtm = [];
	strHtm.push("<select class=\"countPerPage "+ (opt.styleClass?opt.styleClass:'') +"\" name=\"countPerPage\">");

	for(var i = 0 ; i <countArr.length; i++){
		strHtm.push("	<option value=\""+countArr[i]+"\" "+(i==0?'selected':'')+">"+countArr[i]+"</option>");
	}
	
	strHtm.push("</select>");
	$(selector).html(strHtm.join(''));
	
	if(opt.change){
		$(selector+' .countPerPage').on('change',fnChange);
	}
}

/**
 * @method _$base.toast
 * @description toast
 */	
_$base.toast = {
	options : {
		info :  {
			icon : 'info'
			, bgColor: '#000'
		}
		,error : {
			icon : 'error'
			, bgColor: '#ee8777'
		}
		,warning :{
			icon : 'warning'
			, bgColor: '#d9b36c'
		}
		,success:{
			icon : 'success'
			, bgColor: '#7399e6'
		}
		,alert : {
			icon: 'warning'
			, bgColor: '#7399e6'
		}
		,text :{
			bgColor: '#7399e6'
		}
	}
	/**
	 * @method _$base.dialog.view
	 * @param opt {Object} toast option
	 * @description toast view
	 */	
	,view :function (option){
		var opt = this.options[option.icon]; 
		opt = opt?opt : this.options['info'];
		
		// 기본 옵션 셋팅
		var setOpt = PubEP.util.objectMerge({},globalUIOption.toast,opt);
		
		var _opener = window;
		
		if(typeof top.PubEPUI !=='undefined'){
			_opener = top; 
		}else if(typeof parent.PubEPUI !=='undefined'){
			_opener = parent; 
		}
		
		_opener.$.toast($.extend(true,setOpt, option));
	}
}

/**
 * @method PubEP
 * @description ui 모듈 
 */
_$base.module={
	/**
	 * @method _$base.module.selectBoxMove
	 * @param first {String} 이동할 source
	 * @param second {Object} 이동될 target
	 * @description select box  move
	 */	
	selectBoxMove : function (first,second,opt){
		var defaultOpt = {
			addItemClass:'select'
			,useSelectOption : false
			,dbclick : false
			,firstItem : {
				optVal : 'CODE_ID'
				,optTxt : 'CODE_NM'
				,searchAttrName : '_name'
				,searchAttrKey : ''
				,items: []
				,itemKeyIdx : {}
			}
			,secondItem : {
				optVal : 'CODE_ID'
				,optTxt : 'CODE_NM'
				,items: []
				,itemKeyIdx : {}
			}
			,message : {
				addEmpty : false
				,delEmpty : false
			}
			,beforeMove : false
			,beforeItemMove : false
			,afterFirstMove : false
			,beforeSecondMove : false
			,afterSecondMove : false
			,maxSize : -1
			,maxSizeMsg : false
		};

		if(!$(first).hasClass('pub-select-box-first')){
			$(first).addClass('pub-select-box-first');
		}

		if(!$(second).hasClass('pub-select-box-second')){
			$(second).addClass('pub-select-box-second');
		}
		
		var actionObj ={
			firstSelect : first
			,secondSelect : second
			,addItemList :{}
			,options : {}
			,init:function (){
				var _this = this; 
				_this.options = $.extend(true,defaultOpt,opt);
				_this._initItem();

				return _this; 
			}
			/**
			 * @method _$base.selectBoxMove()._initItem
			 * @description selectbox 정보 초기화 
			 */	
			,_initItem : function (){
				var _this = this
				
				_this.setItem('second',_this.options.secondItem.items);
				_this.setItem('first',_this.options.firstItem.items);
			}
			/**
			 * @method _$base.selectBoxMove().setItem
			 * @param type {String} selectbox 타입(first or second)
			 * @param items {Array} items array
			 * @description item 그리기.
			 */		
			,setItem :  function (type , items){
				var _this = this
					,_opts = _this.options
					,tmpFirstItem = _opts.firstItem
					,strHtm = []
					,tmpItem;
				
				var len ,valKey ,txtKey 
					,searchAttrName = tmpFirstItem.searchAttrName
					,searchAttrKey = tmpFirstItem.searchAttrKey == '' ? txtKey : tmpFirstItem.searchAttrKey;

				if(type=='first'){
					tmpFirstItem.items = items;
					len = tmpFirstItem.items.length;
					valKey = tmpFirstItem.optVal;
					txtKey = tmpFirstItem.optTxt;
						
					if(_this.options.useSelectOption===true){
						_this.options.firstItem.items=[];
						$(_this.firstSelect +' option').each(function (i ,item){
							var sObj = $(this);
							var addItem = {};
	
							addItem[valKey] = sObj.val();
							addItem[txtKey] = sObj.text();
							addItem[searchAttrName] = sObj.attr(searchAttrName);
														
							if(_this.addItemList[addItem[valKey]]){
								sObj.addClass(_this.options.addItemClass);
							}
							
							_this.options.firstItem.items.push(addItem);
							_this.options.firstItem.itemKeyIdx[addItem[valKey]] = i;
						});
					}else{
						for(var i=0 ;i < len; i++){
							tmpItem = tmpFirstItem.items[i];
							var tmpSelctOptVal = tmpItem[valKey]; 

							strHtm.push(_this.getItemHtml(type,tmpSelctOptVal, tmpItem));
	
							_this.options.firstItem.itemKeyIdx[tmpSelctOptVal] = i;
						}

						$(_this.firstSelect).empty().html(strHtm.join(''));
					}
				}else{
					var tmpSecondItem= _opts.secondItem
					tmpSecondItem.items = items; 
					len = tmpSecondItem.items.length;
					valKey = tmpSecondItem.optVal;
					txtKey = tmpSecondItem.optTxt;
					_this.addItemList={};
					
					if(_this.options.useSelectOption===true){
						_this.options.secondItem.items=[];
						var idx = 0; 
						$(_this.secondSelect +' option').each(function (i ,item){
							var sObj = $(this);
							var addItem = {};
	
							addItem[valKey] = sObj.val();
							addItem[txtKey] = sObj.text();
							addItem[searchAttrName] = sObj.attr(searchAttrName);
														
							var _key = addItem[valKey]; 
							addItem['_CU'] = 'U';
							_this.addItemList[_key] =addItem;
							_this.options.secondItem.items.push(addItem);
							
							$(_this.firstSelect+' option[value="'+addItem[valKey] +'"]').addClass(_this.options.addItemClass);
							++idx;
						});
						len = idx; 
					}else{
						for(var i=0 ;i < len; i++){
							tmpItem = tmpSecondItem.items[i];
							
							var tmpSelctOptVal = tmpItem[valKey]; 
							tmpItem['_CU'] = 'U';
							_this.addItemList[tmpSelctOptVal] =tmpItem;
							strHtm.push(_this.getItemHtml(type,tmpSelctOptVal, tmpItem));
							
							$(_this.firstSelect+' option[value="'+tmpSelctOptVal+'"]').addClass(_this.options.addItemClass);
						}

						$(_this.secondSelect).empty().html(strHtm.join(''));
					}
					
					if(len < 1){
						$(_this.firstSelect+' option').removeClass(_this.options.addItemClass);
					}
				}
				_this.initEvent();
			}
			/**
			 * @method _$base.selectBoxMove().getItemHtml
			 * @param type {String} selectbox 타입(first or second)
			 * @param seletOptVal {String} 선택한 item 값
			 * @param tmpItem {Object} item
			 * @description item 그리기.
			 */	
			,getItemHtml: function (type  , seletOptVal , tmpItem){
				var _this = this
					, _opts = _this.options
					, firstItem = _opts.firstItem
					, txtKey = firstItem.optTxt
					, searchAttrName = firstItem.searchAttrName
					, searchAttrKey = firstItem.searchAttrKey == '' ? txtKey : firstItem.searchAttrKey;
			
				if(type=='first'){
					var styleClass = 'pub-option-item';
					if(_this.addItemList[seletOptVal]){
						styleClass += ' '+_this.options.addItemClass; 
					}

					return '<option value="'+seletOptVal+'" '+searchAttrName+'="'+escape(tmpItem[searchAttrKey])+'" class="'+styleClass+'">'+tmpItem[txtKey]+'</option>'; 
				}else{
					return '<option value="'+seletOptVal+'" '+searchAttrName+'="'+escape(tmpItem[searchAttrKey])+'" class="pub-option-item">'+tmpItem[txtKey]+'</option>'; 
				}
			}
			/**
			 * @method _$base.selectBoxMove().initEvent
			 * @description 이벤트 초기화.
			 */		
			,initEvent:function (){
				var _this = this; 
				
				var dbclick  =false;
				
				if(_this.options.dbclick===true){
					$(_this.firstSelect).off('dblclick');
					$(_this.firstSelect).on("dblclick",  function(){
						actionObj.firstMove();
					});
					
					$(_this.secondSelect).off('click');
					$(_this.secondSelect).dblclick(function (e){
						dbclick = true;
						
						actionObj.secondMove();
						
						setTimeout(function (){
							dbclick = false ; 
						},400);
					});
				}
				
				if($.isFunction(_this.options.secondItemClick)){
					$(_this.secondSelect).click(function(e){
						setTimeout(function (){
							if(dbclick == false){
								var selectElement = $(_this.secondSelect+' option:selected:first');
								_this.options.secondItemClick({item : _this.getAddItem(selectElement.val()) ,element: selectElement});
							}
						},300);
					});
				}
			}
			/**
			 * @method _$base.selectBoxMove().allFirstMove
			 * @description 첫번재 item 모두 이동
			 */		
			,allFirstMove:function (){
				var _this = this; 
				$(_this.firstSelect).children().each(function (i, item){
					$(_this.secondSelect).append('<option value="'+$(item).val()+'">'+$(item).html()+'</option>');
					$(item).remove();
				});
			}
			/**
			 * @method _$base.selectBoxMove().firstMove
			 * @description 첫번재 item 이동
			 */		
			,firstMove:function (){
				var _this = this; 
				var selectVal = $(_this.firstSelect +' option:selected');
				
				if($.isFunction(_this.options.beforeMove)){
					if(_this.options.beforeMove('first') === false){
						return ; 
					};
				}
				
				if(selectVal.length >0){
					var tmpVal = '',tmpObj;
					var addItemArr = [];
					selectVal.each(function (i, item){
						tmpObj = $(item);
						tmpVal=tmpObj.val();
						
						if(tmpVal=='_no_data_'){
							return true; 
						}
						if(_this.addItemList[tmpVal]){
							return ; 
						}
						
						if($.isFunction(_this.options.beforeItemMove)){
							if(_this.options.beforeItemMove(tmpObj) === false){
								return ; 
							}; 
						}
						
						if(_this.options.maxSize != -1  && $(actionObj.secondSelect+' option').length >= _this.options.maxSize){
							
							if($.isFunction(_this.options.maxSizeMsg)){
								_this.options.maxSizeMsg();
							}
							
							return false; 
						}

						var selectItem = _this.options.firstItem.items[_this.options.firstItem.itemKeyIdx[tmpVal]]; 
						var _addItem = $.extend(true , {}, selectItem);
						_addItem['_CU'] = 'C';
						_this.addItemList[tmpVal] =_addItem;
						$(actionObj.secondSelect).append(_this.getItemHtml('second',tmpVal ,selectItem ));
						tmpObj.addClass(_this.options.addItemClass);
												
						addItemArr.push(tmpVal);
											
						if($.isFunction(_this.options.afterFirstMove)){
							_this.options.afterFirstMove(tmpObj); 
						}
					});
					
					return addItemArr;
				}else{
					if(_this.options.message.addEmpty !== false){
						alert(_this.options.message.addEmpty);
					}
					return ;
				}
			}
			/**
			 * @method _$base.selectBoxMove().secondMove
			 * @description 두번째 item 삭제
			 */	
			,secondMove:function (){
				var _this = this; 
				var selectVal = $(_this.secondSelect +' option:selected');
				
				if($.isFunction(_this.options.beforeMove)){
					if(_this.options.beforeMove('second') === false){
						return ; 
					};
				}

				if(selectVal.length >0){
					var removeItem;
					
					var removeItemArr = [];
					selectVal.each(function (i, item){
						if($.isFunction(_this.options.beforeSecondMove)){
							_this.options.beforeSecondMove($(item)); 
						}
						var tmpKey = $(item).val(); 
						removeItem = _this.options.firstItem.items[_this.options.firstItem.itemKeyIdx[tmpKey]];
						if(removeItem){
							$(actionObj.firstSelect+' option[value="'+tmpKey+'"]').removeClass(_this.options.addItemClass);
						}
						$(item).remove();
						
						removeItemArr.push(tmpKey);
						
						delete _this.addItemList[tmpKey];
						
						if($.isFunction(_this.options.afterSecondMove)){
							_this.options.afterSecondMove(removeItem); 
						}
					});
					
					return removeItemArr;
				}else{
					if(_this.options.message.delEmpty !== false){
						alert(_this.options.message.delEmpty);
					}
					return ;
				}
			}
			/**
			 * @method _$base.selectBoxMove().getAddItem
			 * @description 추가된 아이템 구하기.
			 */	
			,getAddItem:function (itemKey){
				var  _this = this;
				if(itemKey){
					return _this.addItemList[itemKey]; 
				}else{
					var reInfo =[];
					$(_this.secondSelect).children().each(function (i, item){
						reInfo.push(_this.addItemList[$(item).val()]); 
					});
				}

				return reInfo;
			}
			/**
			 * @method _$base.selectBoxMove().getAddItem
			 * @param type {String} up or down
			 * @description 두번째 selectbox 아래위 이동. 
			 */	
			,addItemStausUpdate : function (itemKey){
				if(this.addItemList[itemKey]){
					this.addItemList[itemKey]['_CU'] = 'CU';
				}
			}
			/**
			 * @method _$base.selectBoxMove().getAddItem
			 * @param type {String} up or down
			 * @description 두번째 selectbox 아래위 이동. 
			 */	
			,move:function (type){
				var _this = this; 
				var selectElement = $(_this.secondSelect+' option:selected');
				var len = selectElement.length;  
				if(len ==0) return ; 
					
				if(type=='up'){
					for(var i =0 ;i <len ;i++){
						var currItem = $(selectElement[i])
							,prevItem = $(currItem.prev());

						if(!prevItem.is(':selected')) {
							currItem.after(prevItem);
						}
					}
				}else{
					for(var i =len-1 ;i >=0 ;i--){
						var currItem = $(selectElement[i])
							,nextItem = $(currItem.next());

						if(!nextItem.is(':selected')) {
							currItem.before(nextItem);
						}
					}
				}
			}
			/**
			 * @method _$base.selectBoxMove().lSearch
			 * @param type {String} 검색할 문자열
			 * @description 왼쪽 아이템 조회. 
			 */	
			,lSearch : function (val){
				var _this = this,_opts = _this.options;  
				
				var tmpVal = val;
				var searchAttr = _this.options.firstItem.searchAttrName;

				var len = _opts.firstItem.items.length
					,valKey = _opts.firstItem.optVal
					,txtKey = _opts.firstItem.optTxt
					,searchAttrName = _opts.firstItem.searchAttrName
					,searchAttrKey = (_opts.firstItem.searchAttrKey == '' ? txtKey : _opts.firstItem.searchAttrKey)
					,tmpItem
					,strHtm = [];

				if( len> 0){
					for(var i=0 ;i < len; i++){
						tmpItem = _opts.firstItem.items[i];

						if(tmpVal=='' || tmpItem[searchAttrKey].indexOf(tmpVal) > -1){
							strHtm.push(_this.getItemHtml('first', tmpItem[valKey],tmpItem));
						}
					}
					$(_this.firstSelect).empty().html(strHtm.join(''));
				}
			}
			,rSearch : function (val){
				
				$(this.secondSelect).children().each(function (i, item){
					reInfo.push($(item).val())
				});

				return reInfo;
			}
		}
		
		return actionObj.init(); 
		
		
	}
	/**
	 * @method _$base.module.getSelectItem
	 * @param selector {String}  selecotr
	 * @description select box 모든 item 구하기.
	 */	
	,getSelectItem :function (selector, options){
		var reInfo = new Array();
		var attr = options.attr;
		var addItem;
		$(selector).children().each(function (i, item){
			addItem ={};
			var sObj = $(item), tmpAttr; 
			for(var i = 0 ; i <attr.length;i++){
				tmpAttr = attr[i];
				addItem[tmpAttr.rename] = sObj.attr(tmpAttr.key)||'';
			}
			addItem['value'] = $(item).val(); 
			addItem['nm'] = $(item).text(); 
			reInfo.push(addItem);
		});

		return reInfo;
	}
	/**
	 * @method _$base.module.selectItemUpDown
	 * @param selector {String} selecotr
	 * @param type {String} 이동할 타입 (위, 아래)
	 * @description select box  move
	 */	
	,selectItemUpDown : function (selector,type){
		var _this = this; 
		var selectElement = $(selector+' option:selected');
		var len = selectElement.length;  
		if(len ==0) return ; 

		if(type=='up'){			
			var firstIdx = $(selector+' option').index(selectElement[0]);

			if(firstIdx < 1) return ;
			
			$(selectElement[len-1]).after($(selector+' option').get(firstIdx-1));
		}else{
			var lastIdx = $(selector+' option').index(selectElement[len-1]);
			var len = $(selector+' option').length; 
			
			if(lastIdx == len) return ; 

			$(selectElement[0]).before($(selector+' option').get(lastIdx+1));
		}
	}
};

/**
 * @method editor
 * @description 네이버 스마트 에디터 사용시 editor 모듈 사용
 */
_$base.editor ={
	// 에디터 object
	_cacheEditorObject:{}
	// 에디터 element id
	,elPlaceHolderID : 'epEditorArea'
	// 에디터 옵션
	,defalutOptions : {
		oAppRef: [],
		sSkinURI: "SmartEditor2Skin.html",	
		htParams : {
			bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
			//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
			fOnBeforeUnload : function(){
				//alert("완료!");
			}
		}, //boolean
		fOnAppLoad : function(){
			//예제 코드
			//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
		},
		fCreator: "createSEditor2"
	}
	// 기본 아이디값.
	,_getElPlaceHolderID :function (editID){
		return editID||this.elPlaceHolderID; 
	}
	/**
	 * @method __$base.editor.create
	 * @param editorId {String} editor id
	 * @param opt {String} 에디터 옵션.
	 * @description 에디터 그리기.
	 */	
	,create :function (editorId,opt){
		var _this = this;
		
		if(typeof opt ==='undefined'){
			opt ={};
		}
		
		var elId = _this._getElPlaceHolderID(editorId);
		
		_this._cacheEditorObject[elId] = [];
		
		opt['oAppRef']= _this._cacheEditorObject[elId];
		opt['elPlaceHolder'] = elId;
		nhn.husky.EZCreator.createInIFrame($.extend({} ,_this.defalutOptions ,opt));
	}
	/**
	 * @method __$base.editor.setConent
	 * @param content {String} set할 컨텐츠 내용.
	 * @param editorId {String} 에디터 id
	 * @description 에디터 content 추가.
	 */	
	,setConent :function (content,editorId){
		var _this = this; 
		
		var elId = _this._getElPlaceHolderID(editorId);
		
		var addContArr = [];
		if(!$.isArray(content)){
			addContArr.push(content);
		}else{
			addContArr = content;
		}
		
		_this._cacheEditorObject[elId].getById[elId].exec("PASTE_HTML", addContArr);
	}
	/**
	 * @method __$base.editor.getConent
	 * @param editorId {String} 에디터 id
	 * @description 에디터 내용 보기.
	 */	
	,getConent :function (editorId){
		var _this = this;
		
		var elId = _this._getElPlaceHolderID(editorId);
		
		return _this._cacheEditorObject[elId].getById[elId].getIR();
	}
}

window.PubEPUI = _$base;
})( window ,jQuery, PubEP);

(function($) {
	$.fn.pagingNav = function(options, callback) {
		if (!options) {
			$(this).empty();
			return false;
		}
		var currP = options.currPage;
		if (currP == "0")
			currP = 1;
		var preP_is = options.prePage_is;
		var nextP_is = options.nextPage_is;
		var currS = options.currStartPage;
		var currE = options.currEndPage;
		if (currE == "0")
			currE = 1;
		var nextO = 1 * currP + 1;
		var preO = currP - 1;
		var strHTML = new Array();
		strHTML.push('<div class="text-center">');
		strHTML.push(' <ul class="pagination">');
		if (new Boolean(preP_is) == true) {
			strHTML
					.push(' <li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
		} else {
			if (currP <= 1) {
				strHTML
						.push(' <li class="disabled"><a href="javascript:">&laquo;</a></li>');
			} else {
				strHTML
						.push(' <li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
			}
		}
		var no = 0;
		for (no = currS * 1; no <= currE * 1; no++) {
			if (no == currP) {
				strHTML
						.push(' <li class="active"><a href="javascript:">'
								+ no + '</a></li>');
			} else {
				strHTML
						.push(' <li><a href="javascript:" class="page-click" pageno="'+no+'">'
								+ no + '</a></li>');
			}
		}
		if (new Boolean(nextP_is) == true) {
			strHTML
					.push(' <li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');
		} else {
			if (currP == currE) {
				strHTML
						.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
			} else {
				strHTML
						.push(' <li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');
			}
		}
		strHTML.push(' </ul>');
		strHTML.push('</div>');

		this.empty().html(strHTML.join(''));
		
		var initFlag = this.data('initFlag');
		
		if(initFlag != 'true'){
			this.data('initFlag', 'true')
			this.on('click',' .page-click', function() {
				var sNo = $(this).attr('pageno');
				
				
				if (typeof callback == 'function') {
					callback(sNo);
				} else {
					try {
						nextPage(sNo);
					} catch (e) {
					}
				}
			});
		}
		
		return this; 
	};
})(jQuery);


(function($, _$base){

	$.fn.imgPopupView = function(options) {

		var defaults = {  
		  	srcAttrName : 'img_src'
		  	,destroy: false
			,imgEvent : 'click'
			,maxWidth : 1024
			,maxHeight: 768
		  	
	  	};  
	 	var opts = $.extend(defaults, options);
	 	
		return this.each(function(i,item){
			var tmpThis = $(this);
			// Remove all Fullsize bound events
			if(opts.destroy == true){
				tmpThis.unbind();
			} else {
				tmpThis.on(opts.imgEvent, function (){
					var imgSrc = '';
					if(item.tagName=='IMG'){
						imgSrc = $(this).attr('src');
					}else{
						imgSrc = $(this).attr(opts.srcAttrName);
					}
					var strHtm = [];
					strHtm.push("<html>");
					strHtm.push("<head>");
					strHtm.push("<title>IMG VIEW</title>");
					strHtm.push("<style>body{margin:0;cursor:hand;}</style>");
					strHtm.push("</head>");
					strHtm.push("<body scroll=auto onload='var width1=document.all.Timage.width;if(width1>"+opts.maxWidth+")width1="+opts.maxWidth+";var height1=document.all.Timage.height;if(height1>"+opts.maxHeight+")height1="+opts.maxHeight+";top.window.resizeTo(width1+20,height1+70);' onclick='top.window.close();'>");
					strHtm.push("<img src=\""+imgSrc+"\" title='클릭하시면 닫힙니다.' name='Timage' id='Timage'>");
					strHtm.push("</body>");
					strHtm.push("</html>");
					
					var aaa =_$base.util.popupPosition(opts.maxWidth,opts.maxHeight); 

					var imagez = window.open('', "image", "width="+ 100 +"px, height="+ 100 +"px, top="+aaa.top+"px,left="+aaa.left+"px,scrollbars=auto,resizable=1,toolbar=0,menubar=0,location=0,directories=0,status=1");
					imagez.document.open(); 
					imagez.document.write(strHtm.join('')) 
					imagez.document.close(); 
				});
			}
		});
	}
	
	$.fn.errimgload = function(imgOpt) {
		var changeImg = '/images/srch/no_image.gif';
		if(imgOpt){
			if(typeof imgOpt ==='string'){
				changeImg =imgOpt; 
			}else if(typeof imgOpt ==='object'){
				changeImg = imgOpt.changeImg?imgOpt.changeImg:changeImg;
			}
		}
		var base = this; 
		
		return base.each(function() {
			if (this.tagName.toLowerCase() != 'img') return;
			
			var $_orig = $(this);

			$_orig.one('error', function() {
				$_orig.attr('src',changeImg);
			});

			return ; 
		})
	};
})(jQuery, PubEP);

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

    var $parent = selector && $(selector)

    return $parent && $parent.length ? $parent : $this.parent()
  }

  Dropdown.prototype.clearMenus= function (e) {
    var _this = this; 
    if (e && e.which === 3) return
    
    $(backdrop).remove()
    $(toggle).each(function () {
      var $this         = $(this)
      var $parent       = getParent($this)
      var relatedTarget = { relatedTarget: this }

      if (!$parent.hasClass('open')) return

      if (e && e.type == 'click' && /input|textarea/i.test(e.target.tagName) && $.contains($parent[0], e.target)) return

      $parent.trigger(e = $.Event('hide.bs.dropdown', relatedTarget))
      
      
      if (e.isDefaultPrevented()) return
 
	  if($this.data('bs.dropdown') && $this.data('bs.dropdown')._options && $.isFunction($this.data('bs.dropdown')._options.afterClick)){
		$this.data('bs.dropdown')._options.afterClick($this);
	  }
      $this.attr('aria-expanded', 'false').removeClass('on');
      
      $('.bs-dropdown-iframe-cover').hide();
      
      $parent.removeClass('open').trigger($.Event('hidden.bs.dropdown', relatedTarget))
    })
  }

  Dropdown.prototype.toggle = function (e) {
    var _this = this; 
    var $this = $(this)

    if ($this.is('.disabled, :disabled')) return

    var $parent  = getParent($this)
    var isActive = $parent.hasClass('open')
	var _drop_down_idx = $this.attr('_drop_down_idx'); 
	var _pdrop_down_idx = $parent.attr('_drop_down_idx'); 

	if(Dropdown.selectParent != $parent.selector || typeof _pdrop_down_idx==='undefined' || _drop_down_idx == _pdrop_down_idx){
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
      $parent.trigger(e = $.Event('show.bs.dropdown', relatedTarget))

      if (e.isDefaultPrevented()){
 		 if($this.attr('bgiframe')=='true') $($parent.find('.dropdown-menu')).bgiframe();
 		return
 	  }
	  

	  if($this.data('bs.dropdown') && $this.data('bs.dropdown')._options && $.isFunction($this.data('bs.dropdown')._options.beforeClick)){
		$this.data('bs.dropdown')._options.beforeClick($this);
	  }
	
      $this
        .trigger('focus')
        .attr('aria-expanded', 'true')
		.addClass('on');
      
      $($this.data('bs.dropdown') && $this.data('bs.dropdown')._options.iframeCoverSelector).each(function (i, item){
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
        .trigger($.Event('shown.bs.dropdown', relatedTarget))
	
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
		,iframeCoverSelector : '#eportal_main_iframe'
	}, option);
    return this.each(function (i,item) {
      var $this = $(this)
      var data  = $this.data('bs.dropdown')
	  $this.attr('bgiframe',option.bgiframe);
	  $this.attr('_drop_down_idx',getUUID());
	  
	  if (!data) {
		  $this.data('bs.dropdown', (data = new Dropdown(this,option)))
		  $this.data('bs.dropdown')['_options'] = option;
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
	.on('click.bs.dropdown.data-api', '.dropdown-menu :not(a)', function (e) {
		if(e.target.nodeName.toLowerCase() !='a'){
			e.stopPropagation();
		}
	})
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
