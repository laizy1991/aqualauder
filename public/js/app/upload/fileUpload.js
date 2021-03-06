/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload');
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');


    window.onload = function () {
        window.swfu = new SWFUpload({
            upload_url: "/ajax.goodsctrl/upload",
            use_query_string:true,
            // File Upload Settings
            file_size_limit : "1 MB",	// 文件大小控制
            file_types : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
            file_types_description : "All Files",
            file_upload_limit : "0",
            file_post_name: 'fileData',
            file_queue_error_handler : fileQueueError,
            file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
            file_queued_handler : fileQueued,
            upload_progress_handler : uploadProgress,
            upload_error_handler : uploadError,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            button_placeholder_id : "spanButtonPlaceholder",
            button_width: 76,
            button_height: 40,
            //button_text : '<span class="button">请选择文件</span>',
            //button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
            button_text_top_padding: 0,
            button_text_left_padding: 18,
            button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
            button_cursor: SWFUpload.CURSOR.HAND,

            // Flash Settings
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",

            custom_settings : {
                upload_target : "divFileProgressContainer"
            },
            debug: false
        });
    };

    function fileQueueError(file, errorCode, message) {
        try {

            switch (errorCode) {
                case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    imageName = dd.alert("文件大小为0");
                    break;
                case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                    imageName = dd.alert("文件大小为超过" + swfu.settings.file_size_limit);
                    break;
                case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    dd.alert("文件格式错误")
                    break;
                case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                    dd.alert("一次上传太多文件")
                    break;
                default:
                    dd.alert(message);
                    break;
            }

        } catch (ex) {
            this.debug(ex);
        }
    }

    /**
     * 当文件选择对话框关闭消失时，如果选择的文件成功加入上传队列，
     * 那么针对每个成功加入的文件都会触发一次该事件（N个文件成功加入队列，就触发N次此事件）。
     * @param {} file
     * id : string,			    // SWFUpload控制的文件的id,通过指定该id可启动此文件的上传、退出上传等
     * index : number,			// 文件在选定文件队列（包括出错、退出、排队的文件）中的索引，getFile可使用此索引
     * name : string,			// 文件名，不包括文件的路径。
     * size : number,			// 文件字节数
     * type : string,			// 客户端操作系统设置的文件类型
     * creationdate : Date,		// 文件的创建时间
     * modificationdate : Date,	// 文件的最后修改时间
     * filestatus : number		// 文件的当前状态，对应的状态代码可查看SWFUpload.FILE_STATUS }
     */
    function fileQueued(file){
        addReadyFileInfo(file.id,file.name,"");
        //addImage(file);

    }
    function fileDialogComplete(numFilesSelected, numFilesQueued) {
        try {
            if (numFilesQueued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadProgress(file, bytesLoaded) {

        try {
            var percent = Math.ceil((bytesLoaded / file.size) * 100);

            var progress = new FileProgress(file,  this.customSettings.upload_target);
            progress.setProgress(percent);
            if (percent === 100) {
                progress.setStatus("");//正在创建缩略图...
                progress.toggleCancel(false, this);
            } else {
                progress.setStatus("正在上传...");
                progress.toggleCancel(true, this);
            }
        } catch (ex) {
            this.debug(ex);
        }
    }
    iconIndex = 0;
    function uploadSuccess(file, serverData) {
        try {
            serverData = JSON.parse(serverData)
            if(serverData.success) {
                addFileInfo(file.id,"文件上传完成");
                var temp = $('#addGoodIconTmpl').html();
                temp = temp.replace("{index}", iconIndex);
                temp = temp.replace("{value}", serverData.message);
                temp = temp.replace("{src}", serverData.message);
                var ele = $(temp);
                $("#imgSorter").append(ele);

                iconIndex++;
            } else {
                addFileInfo(file.id,"文件上传失败");
            }

        } catch (ex) {
            this.debug(ex);
        }
    }

    function addFileInfo(fileId,message){
        var row = document.getElementById(fileId);
        row.cells[2].innerHTML = "<font color='green'>"+message+"</font>";
    }

    window.deleteFile = function (fileId){
        //用表格显示
        var infoTable = document.getElementById("infoTable");
        var row = document.getElementById(fileId);
        infoTable.deleteRow(row.rowIndex);
        swfu.cancelUpload(fileId,false);
    }

    function addReadyFileInfo(fileid,fileName,message,status){
        //用表格显示
        var infoTable = document.getElementById("infoTable");
        var row = infoTable.insertRow();
        row.id = fileid;
        var col1 = row.insertCell();
        var col2 = row.insertCell();
        var col3 = row.insertCell();
        var col4 = row.insertCell();
        col4.align = "right";
        col2.innerHTML = fileName;
        if(status!=null&&status!=""){
            col3.innerHTML="<font color='red'>"+status+"</font>";
        }else{
            col3.innerHTML="";
        }
        col4.innerHTML = "<a href='javascript:deleteFile(\""+fileid+"\")'>删除</a>";
        col1.style.width="150";
        col2.style.width="250";
        col3.style.width="80";
        col4.style.width="50";
    }

    function cancelUpload(){
        var infoTable = document.getElementById("infoTable");
        var rows = infoTable.rows;
        var ids = new Array();
        if(rows==null){
            return false;
        }
        for(var i=0;i<rows.length;i++){
            ids[i] = rows[i].id;
        }
        for(var i=0;i<ids.length;i++){
            deleteFile(ids[i]);
        }
    }

    function uploadComplete(file) {
        try {
            /*  I want the next upload to continue automatically so I'll call startUpload here */
            if (this.getStats().files_queued > 0) {
                this.startUpload();
            } else {
                var progress = new FileProgress(file,  this.customSettings.upload_target);
                progress.setComplete();
                progress.setStatus("<font color='red'>所有文件上传完毕!</b></font>");
                progress.toggleCancel(false);
                setTimeout(function(){
                    $("#thumbnails").hide();
                    //$("#fileSelectButton").hide();
                    $("#divFileProgressContainer").hide();
                    $("#imgSorter").show();
                }, 0)
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadError(file, errorCode, message) {
        var imageName =  "<font color='red'>文件上传出错!</font>";
        var progress;
        try {
            switch (errorCode) {
                case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                    try {
                        progress = new FileProgress(file,  this.customSettings.upload_target);
                        progress.setCancelled();
                        progress.setStatus("<font color='red'>取消上传!</font>");
                        progress.toggleCancel(false);
                    }
                    catch (ex1) {
                        this.debug(ex1);
                    }
                    break;
                case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                    try {
                        progress = new FileProgress(file,  this.customSettings.upload_target);
                        progress.setCancelled();
                        progress.setStatus("<font color='red'>停止上传!</font>");
                        progress.toggleCancel(true);
                    }
                    catch (ex2) {
                        this.debug(ex2);
                    }
                case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                    imageName = "<font color='red'>文件大小超过限制!</font>";
                    break;
                default:
                    //alert(message);
                    break;
            }
            addFileInfo(file.id,imageName);
        } catch (ex3) {
            this.debug(ex3);
        }

    }


    function addImage(src) {
        //var newImg = document.createElement("img");
        //newImg.style.margin = "5px";
        //
        //document.getElementById("thumbnails").appendChild(newImg);
        //if (newImg.filters) {
        //    try {
        //        newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
        //    } catch (e) {
        //        // If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
        //        newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
        //    }
        //} else {
        //    newImg.style.opacity = 0;
        //}
        //
        //newImg.onload = function () {
        //    fadeIn(newImg, 0);
        //};
        //newImg.src = src.name;
        //console.log(src)
    }

    function fadeIn(element, opacity) {
        var reduceOpacityBy = 5;
        var rate = 30;	// 15 fps


        if (opacity < 100) {
            opacity += reduceOpacityBy;
            if (opacity > 100) {
                opacity = 100;
            }

            if (element.filters) {
                try {
                    element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
                } catch (e) {
                    // If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
                    element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
                }
            } else {
                element.style.opacity = opacity / 100;
            }
        }

        if (opacity < 100) {
            setTimeout(function () {
                fadeIn(element, opacity);
            }, rate);
        }
    }



    /* ******************************************
     *	FileProgress Object
     *	Control object for displaying file info
     * ****************************************** */

    function FileProgress(file, targetID) {
        this.fileProgressID = "divFileProgress";

        this.fileProgressWrapper = document.getElementById(this.fileProgressID);
        if (!this.fileProgressWrapper) {
            this.fileProgressWrapper = document.createElement("div");
            this.fileProgressWrapper.className = "progressWrapper";
            this.fileProgressWrapper.id = this.fileProgressID;

            this.fileProgressElement = document.createElement("div");
            this.fileProgressElement.className = "progressContainer";

            var progressCancel = document.createElement("a");
            progressCancel.className = "progressCancel";
            progressCancel.href = "#";
            progressCancel.style.visibility = "hidden";
            progressCancel.appendChild(document.createTextNode(" "));

            var progressText = document.createElement("div");
            progressText.className = "progressName";
            progressText.appendChild(document.createTextNode("上传文件: "+file.name));

            var progressBar = document.createElement("div");
            progressBar.className = "progressBarInProgress";

            var progressStatus = document.createElement("div");
            progressStatus.className = "progressBarStatus";
            progressStatus.innerHTML = "&nbsp;";

            this.fileProgressElement.appendChild(progressCancel);
            this.fileProgressElement.appendChild(progressText);
            this.fileProgressElement.appendChild(progressStatus);
            this.fileProgressElement.appendChild(progressBar);

            this.fileProgressWrapper.appendChild(this.fileProgressElement);
            document.getElementById(targetID).style.height = "75px";
            document.getElementById(targetID).appendChild(this.fileProgressWrapper);
            fadeIn(this.fileProgressWrapper, 0);

        } else {
            this.fileProgressElement = this.fileProgressWrapper.firstChild;
            this.fileProgressElement.childNodes[1].firstChild.nodeValue = "上传文件: "+file.name;
        }

        this.height = this.fileProgressWrapper.offsetHeight;

    }
    FileProgress.prototype.setProgress = function (percentage) {
        this.fileProgressElement.className = "progressContainer green";
        this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
        this.fileProgressElement.childNodes[3].style.width = percentage + "%";
    };
    FileProgress.prototype.setComplete = function () {
        this.fileProgressElement.className = "progressContainer blue";
        this.fileProgressElement.childNodes[3].className = "progressBarComplete";
        this.fileProgressElement.childNodes[3].style.width = "";

    };
    FileProgress.prototype.setError = function () {
        this.fileProgressElement.className = "progressContainer red";
        this.fileProgressElement.childNodes[3].className = "progressBarError";
        this.fileProgressElement.childNodes[3].style.width = "";

    };
    FileProgress.prototype.setCancelled = function () {
        this.fileProgressElement.className = "progressContainer";
        this.fileProgressElement.childNodes[3].className = "progressBarError";
        this.fileProgressElement.childNodes[3].style.width = "";

    };
    FileProgress.prototype.setStatus = function (status) {
        this.fileProgressElement.childNodes[2].innerHTML = status;
    };

    FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
        this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
        if (swfuploadInstance) {
            var fileID = this.fileProgressID;
            this.fileProgressElement.childNodes[0].onclick = function () {
                swfuploadInstance.cancelUpload(fileID);
                return false;
            };
        }
    };
});
