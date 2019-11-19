function commonMethod(targetId,type,content) {
    if (!content) {
        alert("回复的评论不能为空，请输入内容再回复");
        return;
    }
    $.ajax({
        type: "post",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (data) {
            if (data.code == 200) {
                //刷新页面
                window.location.reload();
            } else {
                if (data.code == 2003) {
                    var isAccepted = confirm(data.message);
                    if (isAccepted) {
                        //点击确定 跳到登录
                        window.open("https://github.com/login/oauth/authorize?client_id=645a2f60bd9b2345d890&redirect_uri=http://localhost:9797/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(data.message);
                }
            }
        },
        dataType: "json"
    });
}

//回复问题
function postComment() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    commonMethod(questionId,1,content);
}


//回复评论
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    commonMethod(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function openComment(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //获取二级评论的展开状态  点击按钮如果没有获得attribute
    // 则展开评论 并设置属性 再次点击 获得了attribute
    // 则表示是当前展开状态 便移除in 使其变成折叠状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        console.log(subCommentContainer);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

//点击标签将获取的值放入标签中 以","分割
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var val = $("#tag").val();
    //检索的字符串值没有出现，则结果 -1
    if (val.indexOf(value) == -1) {
        if (val) {
            $("#tag").val(val + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}


function showTags() {
    $("#tags ").show();
}