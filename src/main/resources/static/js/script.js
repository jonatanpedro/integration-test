var restMap = {};
var selectedTable = {};
var mapColumns = {};

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
} );

$( "#btnExecuteMap" ).on( "click", function () {

    let searchUrl = { "url" : $("#restSource").val()};

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/rest/retrieve",
        data: JSON.stringify(searchUrl),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            restMap = data;
            var keys = Object.keys(data);
            for(let i=0; i < keys.length; i++){
                $('#tblrest').append('<tr><td>' + keys[i] + '</td><td>' + data[keys[i]] + '</td></tr>')
            }
            $("#restSource").val("");
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
            var keys = Object.keys(data);
            for(let i=0; i < keys.length; i++){
                $('#tblcolumns').append('<tr id="' + keys[i] + '" ><td>' + keys[i] + '</td><td>' + data[keys[i]] + '</td><td>' + geSelectFields(restMap, keys[i]) + '</td></tr>')
            }
        });
    }
});

function geSelectFields(map, rowname){

    let keys = Object.keys(map);
    let select = "<select id='row-" + rowname + "' class='col-sm-6'>";
    select += "<option> -- </option>";

    for(let i=0; i < keys.length; i++){
        select += "<option>" + keys[i] + "</option>";
    }
    select +="</select>";

    return select;
}
