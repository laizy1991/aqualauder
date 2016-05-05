/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload.queue');
    require('../../thirdParty/swfupload/swfupload');
    require('../../thirdParty/swfupload/handlers');

    window.onload = function () {
        swfu = new SWFUpload({
            upload_url: "<%=uploadUrl.toString()%>",
            post_params: {"name" : "zwm"},
            use_query_string:true,
            // File Upload Settings
            file_size_limit : "10 MB",	// 文件大小控制
            file_types : "*.*",
            file_types_description : "All Files",
            file_upload_limit : "0",

            file_queue_error_handler : fileQueueError,
            file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
            file_queued_handler : fileQueued,
            upload_progress_handler : uploadProgress,
            upload_error_handler : uploadError,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            button_placeholder_id : "spanButtonPlaceholder",
            button_width: 180,
            button_height: 18,
            button_text : '<span class="button">请选择文件</span>',
            button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
            button_text_top_padding: 0,
            button_text_left_padding: 18,
            button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
            button_cursor: SWFUpload.CURSOR.HAND,

            // Flash Settings
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",

            custom_settings : {
                upload_target : "divFileProgressContainer"
            },
            // Debug Settings
            debug: false  //是否显示调试窗口
        });
    };


     startUploadFile = function() {
        swfu.startUpload();
    }
});
