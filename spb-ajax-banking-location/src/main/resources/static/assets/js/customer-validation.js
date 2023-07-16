page.dialogs.elements.formCreate.validate({
    rules: {
        fullNameCreate: {
            required: true,
            maxlength: 20,
        },
        emailCreate: {
            required: true,
            maxlength: 30,
            email:true,
        }
    },
    messages: {
        fullNameCreate: {
            required: 'Tên là bắt buộc',
            maxlength: "Tên tối đa 20 ký tự"
        },
        emailCreate: {
            required: 'Email là bắt buộc',
            maxlength: "Email tối đa 30 ký tự",
            email: "Sai định dạng email",
        }
    },
    errorLabelContainer: "#modalCreate .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalCreate .error-area");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaCreate.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaCreate.removeClass("show").addClass("hide").empty();
            page.dialogs.elements.errorAreaCreate.removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.create();
    }
});
page.dialogs.elements.formUpdate.validate({
    rules: {
        fullNameUpdate: {
            required: true,
        },
        emailUpdate: {
            required: true,
            maxlength: 30
        }
    },
    messages: {
        fullNameUpdate: {
            required: 'Tên là bắt buộc',
        },
        emailUpdate: {
            required: 'Email là bắt buộc',
            maxlength: 'Email tối đa 30 ký tự'
        }
    },
    errorLabelContainer: "#modalUpdate .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalUpdate .error-area");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaUpdate.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaUpdate.removeClass("show").addClass("hide").empty();
            page.dialogs.elements.errorAreaUpdate.removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.update();
    }
});
page.dialogs.elements.formDeposit.validate({
    rules: {
        transactionDeposit: {
            required: true,
            number: true,
            maxlength: 15
        }
    },
    messages: {
        transactionDeposit: {
            required: 'Số tiền không được để trống',
            number: 'Vui lòng nhập số',
            maxlength: 'Tối đa 15 chữ số'
        }
    },
    errorLabelContainer: "#modalDeposit .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalDeposit .error-area");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaDeposit.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaDeposit.removeClass("show").addClass("hide").empty();
            page.dialogs.elements.errorAreaDeposit.removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.deposit();
    }
});
page.dialogs.elements.formWithdraw.validate({
    rules: {
        transactionWithdraw: {
            required: true,
            number: true,
            maxlength: 15,
        }
    },
    messages: {
        transactionWithdraw: {
            required: 'Số tiền không được để trống',
            number: 'Vui lòng nhập số',
            maxlength: 'Tối đa 15 chữ số',
        }
    },
    errorLabelContainer: "#modalWithdraw .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalWithdraw .error-area");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaWithdraw.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaWithdraw.removeClass("show").addClass("hide").empty();
            page.dialogs.elements.errorAreaWithdraw.removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.withdraw();
    }
});
page.dialogs.elements.formTransfer.validate({
    rules: {
        transferAmount: {
            required: true,
            number: true,
            maxlength: 15,
        }
    },
    messages: {
        transferAmount: {
            required: 'Số tiền không được để trống',
            number: 'Vui lòng nhập số',
            maxlength: 'Tối đa 15 chữ số'
        }
    },
    errorLabelContainer: "#modalTransfer .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalTransfer .error-area");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaTransfer.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaTransfer.removeClass("show").addClass("hide").empty();
            page.dialogs.elements.errorAreaTransfer.removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.transfer();
    }
});
