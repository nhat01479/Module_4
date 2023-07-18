const signIn = $('#signIn');
const usernameLog = $('#usernameLog');
const passwordLog = $('#passwordLog');
const btnLogin = $('#btnLogin');

const role = $('#role');
const usernameReg = $('#usernameReg');
const passwordReg = $('#passwordReg');
const rePasswordReg = $('#rePasswordReg');
const btnRegister = $('#btnRegister');

const btnShowPW = $('.showPassword');

const registerErrorArea = $('#formRegister .error-area');

btnShowPW.on('click', () => {
    passwordLog.attr('type',  passwordLog.attr('type') === 'text' ? 'password' : 'text');
})


btnRegister.on('click', () => {
    console.log(2);

    const obj = {
        username: usernameReg.val(),
        password: passwordReg.val(),
        rePassword: rePasswordReg.val(),
        roleId: role.val()
    }

    $.ajax({
        headers: {
            'accept': 'application/json',
            'content-type': 'application/json'
        },
        type: 'POST',
        url: 'http://localhost:28001/api/auth/register',
        data: JSON.stringify(obj)
    })
        .done((data) => {
            alert('Đăng ký tài khoản thành công');

            const u = data.username;
            const p = data.password;


            localStorage.setItem("username", u);
            localStorage.setItem("password", p);
            console.log(localStorage.getItem("username"));
            // signIn.click();
            location.href = "/login";

        })
        .fail((jqXHR) => {
            const responseJSON = jqXHR.responseJSON;

            registerErrorArea.empty();
            let str = '';

            $.each(responseJSON, (k, v) => {
                str += `<label for="${k}">${v}</label>`
            })

            registerErrorArea.append(str).removeClass('hide').addClass('show');

            console.log(jqXHR);
        })

})

btnLogin.on('click', () => {
    const obj = {
        username: usernameLog.val(),
        password: passwordLog.val()
    }

    $.ajax({
        headers: {
            'accept': 'application/json',
            'content-type': 'application/json'
        },
        type: 'POST',
        url: 'http://localhost:28001/api/auth/login',
        data: JSON.stringify(obj)
    })
        .done(() => {
            alert('Đăng nhập thành công');

            location.href = "/customers";
        })
        .fail((jqXHR) => {
            App.showErrorAlert('Sai tai khoan hoac mat khau')
            console.log(jqXHR);
        })
})


$(document).ready(function () {
    let messageElements = document.querySelectorAll(".message");
    let check = false;

    for (let i = 0; i < messageElements.length; i++) {
        if (messageElements[i].innerHTML.length > 0) {
            check = true;
            break;
        }
    }

    if (check) {
        const container = document.getElementById('container');
        container.classList.add("right-panel-active");
    }
});
