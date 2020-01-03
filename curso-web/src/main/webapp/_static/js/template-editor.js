/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function addText(string, elementId) {
    var textarea = jQuery(elementId);
    var caretPos = textarea[0].selectionStart;
    var textAreaTxt = textarea.val();
    var txtToAdd = string;
    textarea.val(textAreaTxt.substring(0, caretPos) + txtToAdd + textAreaTxt.substring(caretPos));
    textarea.focus();
    var id = replaceAll(elementId, '\\\\', '');
    id = replaceAll(id, '#', '');
    $(textarea).removeAttr("data-autosize-on");
    autosize(textarea);
    setCaretPosition(id, caretPos + txtToAdd.length);
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(find, 'g'), replace);
}

function setCaretPosition(elemId, caretPos) {
    var elem = document.getElementById(elemId);

    if (elem != null) {
        if (elem.createTextRange) {
            var range = elem.createTextRange();
            range.move('character', caretPos);
            range.select();
        } else {
            if (elem.selectionStart) {
                elem.focus();
                elem.setSelectionRange(caretPos, caretPos);
            } else
                elem.focus();
        }
    }
}
