/**
 * Created by Administrator on 2015/10/19.
 */
/**
 * 页面打开APP工具
 */
$(document).ready(function() {
    function isWh() {
        var ua = navigator.userAgent;
        return ua.match(/warthog/i);
    };

    function isWeixin() {
        var ua = navigator.userAgent;
        return ua.match(/MicroMessenger/i);
    };
     
    //只有在游易APP或者SDK内才有效
    if( isWh() && prompt("wh://common/isAppInstalled?pkg_name=cn.warthog.playercommunity") == 'true' ){
        $('a.open_app_flag').each(function() {
            $(this).attr('href', '#');
            $(this).click(function(){
               alert("wh://common/launchAppIfExists?pkg_name=cn.warthog.playercommunity&params=1");
            });
        });
    }
});