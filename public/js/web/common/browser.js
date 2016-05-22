/**
 * @description: 工具类
 */


var util = {

    //load js or css
    loadFile: function (url, callback) {
        var elem;
        if (url.match(/\.js/i)) {
            elem = document.createElement("script");
            elem.src = url;
            document.body.appendChild(elem);
        } else {
            elem = document.createElement("link");
            elem.href = url;
            elem.rel = "stylesheet";
            document.head.appendChild(elem);
        }

        elem.onload = elem.onreadystatechange = function() {
            if (!this.readyState || this.readyState == "loaded" || this.readyState == "complete") {
                if (callback) {
                    callback();
                }
            }
        };
    },

    //cookie time:XX天
    setCookie: function (name, value, time) {
        var oDate = new Date();
        oDate.setDate(oDate.getDate()+time);
        document.cookie = name+"="+value+";expires="+oDate;
    },
    getCookie: function (name) {
        var arr = document.cookie.split("; ");
        for(var i=0; i<arr.length; i++){
            var arr2 = arr[i].split("=");
            if(arr2[0] == name){
                return arr2[1];
            }
        }
        return "";
    },
    removeCookie: function (name) {
        util.setCookie(name,"",0);
    },

    //移动端环境判断
    ua: navigator.userAgent,
    url: window.location.href,
    isShare: function () {
        //return this.url.match(/petShareFrom/i);
    },
    isMobile: function () {
        return this.ua.match(/AppleWebKit.*Mobile/i);
    },
    isIos: function () {
        return this.ua.match(/iPhone|iPod|iPad/i);
    },
    isAndroid: function () {
        return this.ua.match(/Android/i)
    },
    isWeixin: function () {
        return this.ua.match(/MicroMessenger/i);
    },
    
    //是否支持收藏功能
    isFavoritable:function(){
    	if( document.all ){
    		return true;
    	}else if(window.sidebar){
    		return true;
    	}
    	return false;    	
    },
    addFavorite:function(title,url){
    	if( document.all ){
	       try{
	          window.external.addFavorite(url, title);
	       }catch(e){
	           return false;
	       }
    	}else if(window.sidebar){
          	try{
            	window.sidebar.addPanel(title, url,''); 
        	}catch(e){
            	return  false;
       		}
       	}
    }
};
