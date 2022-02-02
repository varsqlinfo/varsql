
// $.pubMultiselect default value
try{
	if($.isFunction($.pubMultiselect)){
		$.pubMultiselect.setDefaults({
			i18 : {
				upLabel : VARSQL.messageFormat('up')
				,downLabel : VARSQL.messageFormat('down')
				,add : VARSQL.messageFormat('add')
				,remove : VARSQL.messageFormat('remove')
			}
		})
	}
}catch(e){};