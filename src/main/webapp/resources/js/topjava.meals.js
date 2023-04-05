const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

let filterUrl = "";

function filter() {
    $.ajax({
        url: ctx.ajaxUrl + "filter?" + $('#filter').serialize(),
        type: "GET"
    }).done(function (data) {
        eraseAndReload(data)
        filterUrl = "filter?" + $('#filter').serialize();
    });
}

function updateTable() {
    $.get(ctx.ajaxUrl + filterUrl, function (data) {
        eraseAndReload(data);
    });
}

function clearFilter() {
    filterUrl = "";
    updateTable();
}

