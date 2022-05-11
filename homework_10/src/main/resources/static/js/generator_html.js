const genHtml = {
     getButtons: (buttons, funcClick) => {
        var str = '';
        for (var prop in buttons) {
            var button = buttons[prop];
            if (!button.alt){
                alt = ' alt';
            } else {
                alt = '';
            }
            if (!button.color) {<!--brown, pink, purple, gray, cyan, green, blue, red-->
                button.color='blue';
            }
            str += '<a href="javascript:' + funcClick.name + '(\'' + button.link.method + '\', \'' + button.link.value + '\');" class="button ' + button.color + alt + '">' + button.title + '</a>';
        }
        return str;
    },
    getTable: (labels, rows, funcClick) => {
        var headColumn = '';
        var bodyRows = '';
        for (var prop in labels) {
            headColumn += '<div class="divTableCell">' + labels[prop] + '</div>';
        }
        for (var prop in rows) {
            bodyRows += '<div class="divTableRow selector" id="' + rows[prop].link.value + '" onclick="' + funcClick.name + '(\'' + rows[prop].link.method + '\', \'' + rows[prop].link.value + '\')">';
            var columns = rows[prop].columns;
            for (var c_prop in columns) {
                bodyRows += '<div class="divTableCell">' + columns[c_prop] + '</div>';
            }
            bodyRows += '</div>';
        }
        return '<div class="divTable">'
                + '<div class="divTableBody">'
                    + '<div class="divTableRow divTabTitle">'
                         + headColumn
                    + '</div>'
                    + bodyRows
                + '</div>'
            + '</div>';
    },
    getFields: (fields, isReadonly) => {
        var str = '';
        var readonly = '';
        if (isReadonly) readonly = ' readonly';
        for (var prop in fields) {
            if (fields[prop].type == 'INPUT') {
                if (fields[prop].value === null) fields[prop].value = '';
                str += '<label for="' + fields[prop].name + '">' + fields[prop].label + '</label>'
                     + '<input id="' + fields[prop].name + '" type="text" value="' + fields[prop].value + '" ' + readonly + '/>';
            }
            var opt = '';
            var options = fields[prop].values;
            for (var p in options) {
                opt +='<option value="' + options[p].id + '">' + options[p].value + '</option>';
            }
            if (fields[prop].type == 'SELECT') {
                str += '<label for="' + fields[prop].name + '">' + fields[prop].label + '</label>'
                     + '<select id="' + fields[prop].name + '">' + opt + '</select>';
            }
        }
        return str;
    }
}