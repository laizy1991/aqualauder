/**
 */

define(function() {
    var Initiator = {
        init: function() {
            this.initTableHeader();
        },

        initTableHeader: function() {
            $(document).delegate(".order", "click", function(){
                var orderBy = $("<input>").attr("name", "orderBy");
                orderBy.attr("type", "hidden");
                orderBy.val($(this).attr("data"));
                $("#search-box").append(orderBy);
                if($(this).hasClass("currentOrder")) {
                    var asc = $("#search-box input[name='asc']");
                    asc.val(asc.val()=="false");
                } else {
                    var asc = $("#search-box input[name='asc']");
                    asc.val("false");
                }
                $(".currentFilter").each(function () {
                    var state = $("<input>").attr("name", $(this).attr("field"));
                    state.attr("type", "hidden");
                    state.val($(this).attr("data"));
                    $("#search-box").append(state);
                })

                $("#search-box").submit();
            });

            $(document).delegate(".filter li", "click", function(){
                var thiz = $(this);
                var state = $("<input>").attr("name", $(this).attr("field"));
                state.attr("type", "hidden");
                state.val($(this).attr("data"));

                $(".currentOrder").each(function(){
                    var orderBy = $("<input>").attr("name", "orderBy");
                    orderBy.attr("type", "hidden");
                    orderBy.val($(this).attr("data"));
                    $("#search-box").append(orderBy);
                });

                $(".currentFilter").each(function () {
                    if(thiz.attr("field") != $(this).attr("field")) {
                        var state = $("<input>").attr("name", $(this).attr("field"));
                        state.attr("type", "hidden");
                        state.val($(this).attr("data"));
                        $("#search-box").append(state);
                    }
                })

                $("#search-box").append(state);
                $("#search-box").submit();
            });
        }
    }

    Initiator.init();
});
