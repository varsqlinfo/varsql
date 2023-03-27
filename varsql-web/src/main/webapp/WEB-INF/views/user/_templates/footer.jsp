<%@ page language="java" pageEncoding="UTF-8" %>
<!--  <span class="copy-right">ytechinfo copy right</span> -->
<script>
$(document).ready(function (e){
	if (document.addEventListener) { // IE >= 9; other browsers
        document.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        }, false);
    } else { // IE < 9
        document.attachEvent('oncontextmenu', function(e) {
            window.event.returnValue = false;
        });
    }

	$(document).on('keydown',function (e) {
		var evt =window.event || e;
		if(evt.ctrlKey){
			var returnFlag = true;
			switch (evt.keyCode) {
				case 83: // 83 is s
					returnFlag = false;
					break;
				case 70: // 80 is f
					returnFlag = false;
					break;
				case 80: // 70 is n
					returnFlag = false;
					break;
				default:
					break;
			}
			return returnFlag;
		}
	});
})
</script>