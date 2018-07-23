/**
 * vue.varsql.js v0.0.1
 * ========================================================================
 */
if (typeof window != "undefined") {
    if (typeof window.VarsqlAPP == "undefined") {
        window.VarsqlAPP = {};
    }
}else{
	if(!VarsqlAPP){
		VarsqlAPP = {};
	}
}


var  portalDefaultTemplate = {
	'pageNavTemplate' : '<div class="text-center"><ul class="pagination">'
		+'<li :class="((pageInfo.preP_is !== true && pageInfo.currPage <=1)? \'disabled\' :\'\')">'
		+'	<a @click="goPage(pageInfo.currPage - 1)">«</a>'
		+'</li>'
		+'<li v-for="no in range(pageInfo.currStartPage , pageInfo.currEndPage)" :class="no ==pageInfo.currPage?\'active\':\'\'">'
		+'	<a v-if="no ==pageInfo.currPage">{{no}}</a>'
		+'	<a v-if="no != pageInfo.currPage" @click="goPage(no)">{{no}}</a>'
		+'</li>'
		+'<li :class="((pageInfo.nextPage_is !== true && pageInfo.currPage ==pageInfo.currEndPage)?\'disabled\':\'\')">'
		+'	<a @click="goPage(pageInfo.currPage + 1)">»</a>'
		+'</li>'
		+'</ul></div>'
		
	,'grid1Template' : '<div class="text-center"><ul class="pagination">'
		+'<li :class="((pageInfo.preP_is !== true && pageInfo.currPage <=1)? \'disabled\' :\'\')">'
		+'	<a @click="goPage(pageInfo.currPage - 1)">«</a>'
		+'</li>'
		+'<li v-for="no in range(pageInfo.currStartPage , pageInfo.currEndPage)">'
		+'	<a v-if="no ==pageInfo.currPage">{{no}}</a>'
		+'	<a v-if="no != pageInfo.currPage" @click="goPage(no)">{{no}}</a>'
		+'</li>'
		+'<li :class="((pageInfo.nextPage_is !== true && pageInfo.currPage ==pageInfo.currEndPage)?\'disabled\':\'\')">'
		+'	<a @click="goPage(pageInfo.currPage + 1)">»</a>'
		+'</li>'
	+'</ul></div>'
};




(function( Vue ,portalDefaultTemplate, $) {

Vue.config.devtools = true;
Vue.prototype.$ajax = VARSQL.req.ajax;
	
VarsqlAPP.message ={
	empty : '데이타가 없습니다.'
}

// list component add
Vue.component('list-cont', {
	created :function() {
		var templateName = this.listType+'Template';
		var templateCont = portalDefaultTemplate[templateName]; 

		if(typeof templateCont ==='undefined'){
			templateCont  = portalDefaultTemplate['type1Template'];
		}

		this.$options.template = templateCont;
	}
	,props: {
		list : Array,
		listType : String, 
		columnKey : Object
	}
	,data:function(){
		var sortOrders = {};

		var keyInfo = Vue.util.extend({
			'TITLE' : 'TITLE'
			,'AUTHOR' : 'AUTHOR'
			,'DATE' : 'VIEW_DT'
			,price : 'price'
			,active : 'active'
			,imgSrc : 'imgSrc'
		},this.columnKey);

		return {
			keyInfo : keyInfo
		}
	}
	,methods: {
		titleClick:function(item) {
			this.$parent.detailItem = item;
			//console.log(JSON.stringify(key))
		}
	}
})

// page navigation component add 
Vue.component('page-navigation', {
	template: portalDefaultTemplate.pageNavTemplate,
	props: {
		pageInfo : Object
		,callback : String
	}
	,methods: {
		range : function (start, end) {
			
			if(typeof start ==='undefined') return [];
			
			var reArr = [];
			
			for(start ; start <= end;start++){
				reArr.push(start);
			}
			
			return reArr;
		}
		,goPage : function (pageNo) {

			if(pageNo < 1){
				pageNo =1; 
				return ; 
			}
			
			if(pageNo > this.pageInfo.totalPage){
				pageNo= this.pageInfo.totalPage; 
				return ; 
			}

			if(this.pageInfo.currPage == pageNo){
				return ; 
			}
			this.pageInfo.currPage = pageNo;

			
			var callback = this.$parent[this.callback];
			
			if(typeof callback === 'undefined'){
				callback = this.$parent['search'];
			}

			callback.call(null,pageNo);
		}
	}
})

/**
 * @method VarsqlAPP.addTemplate 
 * @description 템플릿 add
 */	
VarsqlAPP.addTemplate  = function (template){
	if($.isPlainObject(template)){
		for(var key in template){
			if(typeof portalDefaultTemplate[key] ==='undefined'){
				portalDefaultTemplate[key]= template[key];
			}
		}
	}
}

/**
 * @method VarsqlAPP.addMessage
 * @description 메시지 add
 */	
VarsqlAPP.addMessage = function (msg){
	for(var key in msg){
		VarsqlAPP.message[key]= msg[key];
	}
}


var defaultOpt = {
	el: '#varsqlViewArea'
	,data: {
		detailItem :{}
	}
	,mounted  : function() {
		this.init();
		this.search(1);
    }
	,methods:{
		init: function (){}
		,search: function (no){}
	}
}
/**
 * @method addMethod 
 * @description vue method 추가.
 * @param prefix
 * @param opts
 * @param methodObj
 * @returns
 */
function addMethod(prefix , opts){
	var methodObj = opts[prefix]; 
	
	if(typeof methodObj !=='undefined'){
		for(var key in methodObj){
			opts.methods[prefix+'_'+key] = methodObj[key];
		} 
		delete opts[prefix];
	}
	return opts; 
}

VarsqlAPP.vueServiceBean = function (opts){
	var _evts = {} 
		,_srvs = {};
	
	opts = VARSQL.util.objectMerge({},defaultOpt,opts);
	
	var vueObj = new Vue(opts);
	
	$(opts.el).removeClass('display-off')
	
	return vueObj;
}
})(Vue , portalDefaultTemplate, jQuery);