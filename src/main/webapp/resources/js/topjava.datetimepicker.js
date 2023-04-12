jQuery('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});

jQuery('#startTime').datetimepicker({
    format: 'H:i',
    onShow: function (ct) {
        this.setOptions({
            maxTime: jQuery('#endTime').val() ? jQuery('#endTime').val() : false
        })
    },
    datepicker: false
});
jQuery('#endTime').datetimepicker({
    format: 'H:i',
    onShow: function (ct) {
        this.setOptions({
            minTime: jQuery('#startTime').val() ? jQuery('#startTime').val() : false
        })
    },
    datepicker: false
});


jQuery('#startDate').datetimepicker({
    format: 'Y-m-d',
    onShow: function (ct) {
        this.setOptions({
            maxDate: jQuery('#endDate').val() ? jQuery('#endDate').val() : false
        })
    },
    timepicker: false
});
jQuery('#endDate').datetimepicker({
    format: 'Y-m-d',
    onShow: function (ct) {
        this.setOptions({
            minDate: jQuery('#startDate').val() ? jQuery('#startDate').val() : false
        })
    },
    timepicker: false
});