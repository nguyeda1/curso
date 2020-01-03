var jsfbind = function () {
    if ($('#imgdisp').length === 0) {
        $('body').append('<div id="imgdisp" style="display: none; position: absolute; top: 0px; left: 0px;border: 2px solid white; background: white;"><img /></div>');
    }
    $('.hoverresize').hover(function (e) {
        $('#imgdisp img').attr('src', this.src);
        $('#imgdisp').css('top', e.pageY - 10).css('left', e.pageX + 16).fadeIn(100);
    }, function () {
        $('#imgdisp').fadeOut(100);
    });
    $('a.evo').click(function (e) {
        var l = e.currentTarget.className.split(/\s+/), i = 0;
        for (; i < l.length; i++) {
            if (l[i].startsWith("listingid_")) {
                window.open(window.contextPath + "/app/booking/view.xhtml?id=" + l[i].substr(10), "_blank");
            }
        }
    });
    $('.ui-inputtext').each(function (e) {
        $(this).unbind('keydown.slider');
    });
    $('textarea').each(function () {
        var p = $(this).attr('placeholder');
        if (p)
            $(this).attr('placeholder', p.replace(/\\n/g, '\n'))
    })
};
$(function () {

    jsfbind();
});
