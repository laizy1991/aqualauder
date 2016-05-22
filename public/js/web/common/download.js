/**
 * @description: warthog下载链接处理
 */

$(document).ready(function(){
	function isWh() {
        var ua = navigator.userAgent;
        return ua.match(/warthog/i);
    };

    function isWeixin() {
        var ua = navigator.userAgent;
        return ua.match(/MicroMessenger/i);
    };

    if (isWh()) {
	    $('a.download-url').each(function() {
	        var url = $(this).attr('href');

	        var downUrlAttr = $(this).attr("down-url");
	        if (downUrlAttr && downUrlAttr != ""){
	        	url = downUrlAttr;
	        }

	        $(this).attr('href', '#');
	        $(this).click(function(){
	        	alert('wh://common/download?url='+url+"&name=" + url.substring(url.lastIndexOf("/")+1, url.length));
	        });
	    });
	}else{
	    $('a.download-url').each(function() {
	        var url = $(this).attr('href');
	        var downUrlAttr = $(this).attr("down-url");
	        if (downUrlAttr && downUrlAttr != ""){
	        	url = downUrlAttr;
	        }
	        if ( isWeixin() && url == "http://otherfiles.b0.upaiyun.com/app/youyi.apk" ){
	        	url = "http://a.app.qq.com/o/simple.jsp?pkgname=cn.warthog.playercommunity";
	        }
			$(this).attr('href', url);
	    });
	}

    $('a.download-url').click(function(){
        var url = $(this).attr('href');
        if (isWeixin() && url != "http://a.app.qq.com/o/simple.jsp?pkgname=cn.warthog.playercommunity" ){
            //
            var cacheStyle = $("body").attr("style");
            $("body").attr("cache-style",cacheStyle);
            $("body").attr("style","margin:0 auto;")
            $(".wx-pop-warp").show();
        }
        var clickEvent = $(this).attr("click-event");
        if( clickEvent && clickEvent!=""){
            $.post("/downbtn/click?clickEvent="+clickEvent);
        }
    });
});