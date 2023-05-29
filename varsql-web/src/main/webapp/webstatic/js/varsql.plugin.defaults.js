
// $.pubMultiselect default value
try{
	if($.isFunction($.pubMultiselect)){
		$.pubMultiselect.setDefaults({
			i18 : {
				upLabel : VARSQL.message('up')
				,downLabel : VARSQL.message('down')
				,add : VARSQL.message('add')
				,remove : VARSQL.message('remove')
			}
		})
	}
}catch(e){};
