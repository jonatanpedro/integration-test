var restMap = null;
var selectedTable = null;
var mapColumns = null;
var datatypes = null;
var urlBase = null;

function updateRestMap(map){
    let keys = Object.keys(map);
    keys.forEach(function (key, index, array) {
        let row = $("#row-source-" + key).val();
        if (row) {
            if (row !== "--") {
                map[key].destination = row;
            }
        }
    });
    return map;
}

function updateDestinationMap(map){
    let keys = Object.keys(map);
    keys.forEach(function (key, index, array) {
        let row = $("#row-db-" + key).val();
        if (row) {
            if (row !== "--") {
                map[key].source = row;
            }
        }
    });
    return map;
}

$( "#btnSalvar" ).on( "click", function () {

    let flow = {
        urlBase: urlBase.url,
        useTable: $( "#bd" ).is(':checked'),
        useConsole: $( "#console" ).is(':checked'),
        selectedTable: selectedTable,
        sourceMap: updateRestMap(restMap),
        destinationMap: updateDestinationMap(mapColumns),
        serviceType: urlBase.serviceType
    }

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/rest/saveflow",
        data: JSON.stringify(flow),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            $('#successAlert').show();
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
});

$( "#bd" ).on( "click", function () {
    if ($( "#bd" ).is(':checked')) {
        $.get("/rest/tables", function (data) {
            let $select = $('#tabelabd');
            $.each(data, function (key, val) {
                $select.append('<option>' + val + '</option>');
            });
        });
    }
    $( "#tabelabd" ).prop('disabled', !$( "#bd" ).is(':checked'));
    $( "#btnRecColunas" ).prop('disabled', !$( "#bd" ).is(':checked'));
} );

$(document).ready(function() {
    $('#rowOperation').hide();
});

$('input[type=radio][name=serviceType]').change(function() {
    if (this.value == 'REST') {
        $('#rowOperation').hide();
    }
    else {
        $('#rowOperation').show();
    }
});

$( "#btnExecuteMap" ).on( "click", function () {

    urlBase = {
        "url" : $("#serviceSource").val(),
        "serviceType": $('input:radio[name=serviceType]:checked').val()
    };

    var endpoint = '/rest/retrieve-rest';
    if (urlBase.serviceType === 'SOAP') {
        urlBase.operation = $('#operation').val();
        urlBase.port = $('#port').val();
        endpoint = '/rest/retrieve-soap';
    }

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: endpoint,
        data: JSON.stringify(urlBase),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            $.get("/rest/datatypes/", function (dataTypesResult) {
                datatypes = dataTypesResult;
                restMap = data;
                var keys = Object.keys(data);
                for(let i=0; i < keys.length; i++){
                    $('#tblrest').append('<tr><td>' + keys[i] + '</td><td>' + data[keys[i]].source + '</td><td>' + getSelectDataTypes(datatypes, keys[i]) +'</td></tr>')
                }
                $("#serviceSource").val("");
            });
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
});

$( "#btnRecColunas" ).on( "click", function () {
    selectedTable = $("#tabelabd").val();
    if (selectedTable) {
        $.get("/rest/columns/" + selectedTable, function (data) {
            mapColumns = data;
            let keys = Object.keys(data);
            for(let i=0; i < keys.length; i++){
                $('#tblcolumns').append('<tr id="' + keys[i] + '" ><td>' + keys[i] + '</td><td>' + data[keys[i]].destination + '</td><td>' + geSelectFields(restMap, keys[i]) + '</td></tr>')
            }
        });
    }
});


$( "#btnExecuteFlow" ).on( "click", function () {

});


function geSelectFields(map, rowname){

    let keys = Object.keys(map);
    let select = "<select id='row-db-" + rowname + "' class='col-sm-6'>";
    select += "<option>--</option>";

    for(let i=0; i < keys.length; i++){
        select += "<option>" + keys[i] + "</option>";
    }
    select +="</select>";

    return select;
}

function getSelectDataTypes(types, key) {
    let select = "<select id='row-source-" + key + "' class='col-sm-6'>";
    select += "<option>--</option>";

    $.each(types, function (key, val) {
        select += "<option>" + val + "</option>";
    });
    select +="</select>";

    return select;
}
