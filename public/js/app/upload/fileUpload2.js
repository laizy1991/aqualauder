/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload.queue');
    require('../../thirdParty/swfupload/swfupload');
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');


    window.onload = function () {
        var goodsId = $("#goodsId").val()
        window.swfu = new SWFUpload({
            upload_url: "/ajax.goodsctrl/upload",
            use_query_string:true,
            file_size_limit : "1 MB",	// 文件大小控制
            file_types : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
            file_types_description : "All Files",
            file_upload_limit : "0",
            file_post_name: 'fileData',
            file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            button_placeholder_id : "spanButtonPlaceholder",
            button_width: 84,
            button_height: 38,
            //button_text : '<span class="button">请选择文件</span>',
            //button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
            button_text_top_padding: 0,
            button_text_left_padding: 18,
            button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
            button_cursor: SWFUpload.CURSOR.HAND,
            post_params: {"goods.id" : goodsId},

            // Flash Settings
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",

            custom_settings : {
                upload_target : "divFileProgressContainer"
            },
            debug: false
        });
    };

    function fileDialogComplete(numFilesSelected, numFilesQueued) {
        try {
            if (numFilesQueued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadSuccess() {
        if(this.getStats().files_queued == 0) {
            window.location.reload();
        }
    }

    function uploadComplete() {
        try {
            if (this.getStats().files_queued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }




});
