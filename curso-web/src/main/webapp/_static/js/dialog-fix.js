
PrimeFaces.widget.Dialog.prototype.fitViewport = function () {
	var winHeight = $(window).height();
	contentPadding = this.content.innerHeight() - this.content.height();

	if (this.jq.innerHeight() > winHeight) {
		this.content.height(winHeight - this.titlebar.innerHeight() - contentPadding - (this.footer ? this.footer.innerHeight() : 0));
	}
};

//Saves the postShow event in a new event to be used later
PrimeFaces.widget.Dialog.prototype.origPostShow = PrimeFaces.widget.Dialog.prototype.postShow;

//Overwrite the event postShow calling fitViewport event
PrimeFaces.widget.Dialog.prototype.postShow = function () {
	this.fitViewport();
	this.origPostShow();
};