/**
 * @description: 微信分享工具
 */

$(function () {
    var wxShareSpecifyImg = $("#wxShareImg").attr("src"), //特定图片
        wsShareDefaultImg =  "http://www.youyi123.cn/public/images/web/favicon.png", //默认图片
        wsShareImg = wxShareSpecifyImg || wsShareDefaultImg;

    var wxShare = {
        title: $("title").text() || "游易游戏社区|手游充值优惠|刀塔传奇优惠",
        desc: $("meta[name=description]").attr("content") || "",
        img: wsShareImg,
        link: window.location.href,
        url: function () {
            return encodeURIComponent(wxShare.link.split("#")[0]);
        }
    };
    //console.log(wxShare.url());
    define = null;
    require = null;
    
    $.ajax({
        url: "/weixin/js?url="+ wxShare.url(),
        success: function(data){
            util.loadFile("http://res.wx.qq.com/open/js/jweixin-1.0.0.js", function () {
                var shareData = eval(data);
                //配置信息
                wx.config({
                    debug: false,
                    appId: 'wx0ec4c9e4814e0fa5',
                    timestamp: shareData.timestamp,
                    nonceStr: shareData.nonceStr,
                    signature: shareData.signature,
                    jsApiList: [
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage',
                        'onMenuShareQQ',
                        'onMenuShareWeibo'
                    ]
                });

                //分享到...
                wx.ready(function () {
                    wx.onMenuShareTimeline({
                        //title: wxShare.title,
                        title: wxShare.desc,
                        link: wxShare.link,
                        imgUrl: wxShare.img,
                        success: function () {},
                        cancel: function () {}
                    });
                    wx.onMenuShareAppMessage({
                        title: wxShare.title,
                        desc: wxShare.desc,
                        link: wxShare.link,
                        imgUrl: wxShare.img,
                        type: '',
                        dataUrl: '',
                        success: function () {},
                        cancel: function () {}
                    });
                    wx.onMenuShareQQ({
                        title: wxShare.title,
                        desc: wxShare.desc,
                        link: wxShare.link,
                        imgUrl: wxShare.img,
                        success: function () {},
                        cancel: function () {}
                    });
                    wx.onMenuShareWeibo({
                        title: wxShare.title,
                        desc: wxShare.desc,
                        link: wxShare.link,
                        imgUrl: wxShare.img,
                        success: function () {},
                        cancel: function () {}
                    });
                });

            })
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            console.log('Ajax error!')
        }
    });

});
