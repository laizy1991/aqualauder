/**
 * Created by Administrator on 2015/10/28.
 */
$(function(){
    Layout.setW();
    Layout.setPopBox();
    Layout.setMenu();
    Layout.setBalance();
   // Layout.setGiftList();
   // Layout.setColumn();
    Layout.setRelative();
});

$(window).resize(function(){
    Layout.setW();
    Layout.setPopBox();
    Layout.setMenu();
    Layout.setBalance();
   // Layout.setGiftList();
    Layout.setRelative();
});
var w ;
var maxW = 480;
/*布局*/
var Layout = {
    /*显示宽度*/
    setW:function(){
        w =  $(window).outerWidth(true);
        if( w > maxW ){
            $("body").css("max-width",maxW+"px");
            w = maxW;
        }
    },
    /*弹窗*/
    setPopBox:function(){
        if( $(".f-ly-tips-wrap-2").length>0 && $(".f-btn-tips-r-1").length>0 && $(".f-btn-tips-l-1").length>0){
            var tw = w - 40;
            tw = w>400?360:tw;
            $(".f-ly-tips-wrap-2").attr("style","width:"+tw+"px;margin-left:-"+Math.floor(tw/2)+"px;");
            $(".f-ly-tips-2").attr("style","width:"+tw+"px;");
            var btw = Math.floor((tw-1)/2);
            $(".f-btn-tips-l-1").attr("style","width:"+btw+"px;display:block;");
            $(".f-btn-tips-r-1").attr("style","width:"+btw+"px;display:block;");
        }
    },
    /*菜单*/
    setMenu:function(){
        if( $(".f-ly-more-1").length>0){
            var liW = Math.floor(w/3);
            liW = w > 400?130:liW;
            $(".f-ly-more-1 ul li").attr("style","width:"+liW+"px;")
        }
        $(".f-ly-menu-r-1").unbind("click");
        $(".f-ly-menu-r-1").click(function(){
            if( $(".f-ly-more-1").length>0){
                if($(".f-ly-more-1").css("display") == 'none'){
                    $(".f-ly-more-1").show();
                    $(".f-ly-more-1").parent().attr("style","height:96px;");
                }else if($(".f-ly-more-1").css("display") == 'block'){
                    $(".f-ly-more-1").hide();
                    $(".f-ly-more-1").parent().attr("style","height:48px;");
                }
            }
        });
    },
    /**我的余额**/
    setBalance:function(){
        if( $(".f-ly-balance-2-l").length>0 && $(".f-ly-balance-2-r").length>0){
            var bw = Math.floor(w/2);
            $(".f-ly-balance-2-l").attr("style","width:"+bw+"px;");
            $(".f-ly-balance-2-r").attr("style","width:"+bw+"px;");
        }
    },
    setGiftList:function(){
        /*礼包列表宽度适配*/
        if( $(".f-ly-gitf span[f-desc]").length > 0  ){
            $(".f-ly-gitf span[f-desc]").css("width",Math.floor(w - 112)+"px");
        }
        if( $(".f-ly-at-l span[f-desc]").length > 0  ){
            var txW = 104;
            if( $(".f-ly-at-l span[f-btn-disable]").length > 0 || $(".f-ly-at-l span[f-btn-activite]").length > 0 ){

                $(".f-ly-at-l span[f-desc]").css("width",Math.floor(w -txW- 112)+"px");
            }else{

                $(".f-ly-at-l span[f-desc]").css("width",Math.floor(w - txW)+"px");
            }
        }
    },
    setColumn:function(){
        /*列样式宽度==100等分*/
        if( $(".f-ly-column").length>0 ){
            $(".f-ly-column").each(function(i,col){
                    var left = 0;
                    var colHeight = $(col).outerHeight();
                    $(col).find("div[col]").each(function(index,obj){
                        var p = $(obj).attr("col");
                        var v = $(obj).attr("v");
                        var h = $(obj).attr("h");
                        var mt = $(obj).attr("mt")?$(obj).attr("mt"):0;
                        var mr = $(obj).attr("mr")?$(obj).attr("mr"):0;
                        var mb = $(obj).attr("mb")?$(obj).attr("mb"):0;
                        var ml = $(obj).attr("ml")?$(obj).attr("ml"):0;

                        var setFun = function(){

                            var myWidth = Math.floor( (parseInt(p)/100) * w );
                            $(obj).css("width",myWidth + "px");
                            var myHeight = $(obj).outerHeight();
                            if( v ){
                                if( v == "middle"){
                                    mt = Math.floor( (colHeight - myHeight)/2 );
                                    mb = mt;
                                }else if( v == "top"){
                                    mt = 0;
                                }else if( v== "bottom"){
                                    mb = 0;
                                }
                                console.log(colHeight,myHeight,mt,mb);
                            }
                            $(obj).css("margin-top",mt + "px");
                            $(obj).css("margin-bottom",mb + "px");


                            if( h ){
                                if( h == "center"){
                                    mt = Math.floor( (colHeight - myHeight)/2 );
                                    mb = mt;
                                }else if( v == "left"){
                                    mt = 0;
                                }else if( v== "right"){
                                    mb = 0;
                                }
                            }
                        };
                        setFun();
                        $(col).resize(function(){
                            setFun();
                        });
                    });
                }
            );

        }
    },
    /*相对布局*/
    setRelative:function(){
        if( $(".f-ly-relative").length > 0 ){
            $(".f-ly-relative").each(function(i,r){
                $(r).removeAttr("style");
                var hAry = new Array();

                var childs = $(r).children();
                for(var ii = childs.length-1;ii>=0;ii--){
                    var colIndex = isNaN(parseInt($(childs[ii]).attr("col")))?0:parseInt($(childs[ii]).attr("col")) - 1;
                    var tp = isNaN(parseInt($(childs[ii]).attr("top")))?0:parseInt($(childs[ii]).attr("top"));
                    var bt = isNaN(parseInt($(childs[ii]).attr("bottom")))?0:parseInt($(childs[ii]).attr("bottom"));

                    if( hAry[colIndex] ){
                         if( $(childs[ii]).outerHeight()+tp+bt > hAry[colIndex] ){
                             hAry[colIndex] = $(childs[ii]).outerHeight()+tp+bt;
                         }
                    }
                    hAry.push($(childs[ii]).outerHeight()+tp+bt);
                }
                var maxH = hAry[0];
                $.each(hAry,function(i,o){
                    if( o > maxH ){
                        maxH = o;
                    }
                });

                //console.log(hAry);
                $(r).css("height",maxH+"px");
                $(r).children().each(function(ii,child){
                    $(child).removeAttr("style");
                    var childH = $(child).outerHeight();
                    var top = isNaN(parseInt($(child).attr("top")))?0:parseInt($(child).attr("top"));
                    var right = isNaN(parseInt($(child).attr("right")))?0:parseInt($(child).attr("right"));
                    var bottom = isNaN(parseInt($(child).attr("bottom")))?0:parseInt($(child).attr("bottom"));
                    var left = isNaN(parseInt($(child).attr("left")))?0:parseInt($(child).attr("left"));
                    //vertical
                    if( $(child).attr("v") && $(child).attr("v") == "middle"){
                        top = Math.floor( (maxH - childH)/2 );
                        $(child).css("top",top+"px");
                    }else if ( $(child).attr("v") && $(child).attr("v") == "top"){
                        $(child).css("top","0");
                    }else if ( $(child).attr("v") && $(child).attr("v") == "bottom"){
                        $(child).css("bottom","0");
                    }else {
                        if( !isNaN(parseInt($(child).attr("top"))) ){
                            $(child).css("top",top+"px");
                        }
                        if( !isNaN(parseInt($(child).attr("bottom"))) ){
                            $(child).css("bottom",bottom+"px");
                        }
                    }

                    if( $(child).attr("h") && $(child).attr("h") == "center"){
                        left = Math.floor( ( $(r).width() - $(child).width() )/2 );
                        $(child).css("left",left+"px");
                    }else if ( $(child).attr("h") && $(child).attr("h") == "left"){
                        $(child).css("left","0");
                    }else if ( $(child).attr("h") && $(child).attr("h") == "right"){
                        $(child).css("right","0");
                    }else{
                        if( !isNaN(parseInt($(child).attr("left"))) ){
                            $(child).css("left",left+"px");
                        }
                        if( !isNaN(parseInt($(child).attr("right"))) ){
                            $(child).css("right",right+"px");
                        }
                    }
                    $(child).css("position","absolute");
                });
            });
        }
    },
    setEmpty:function(){

    }
}