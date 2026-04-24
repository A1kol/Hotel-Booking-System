let isLogin = true;

function toggleAuth(event) {
    event.preventDefault();
    isLogin = !isLogin;
    const title = document.getElementById('auth-title');
    const mainBtn = document.getElementById('main-btn');
    const toggleText = document.getElementById('toggle-text');

    if (isLogin) {
        title.innerText = 'Grand Hotel';
        mainBtn.innerText = 'Войти';
        toggleText.innerHTML = 'Еще не зарегистрированы? <a href="#" onclick="toggleAuth(event)">Создать аккаунт</a>';
    } else {
        title.innerText = 'Регистрация';
        mainBtn.innerText = 'Зарегистрироваться';
        toggleText.innerHTML = 'Уже есть аккаунт? <a href="#" onclick="toggleAuth(event)">Войти</a>';
    }
}

async function handleSubmit() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const msg = document.getElementById('message');

    const endpoint = isLogin
        ? 'http://localhost:8080/api/auth/login'
        : 'http://localhost:8080/api/auth/register';

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                mail: email,
                password: password
            })
        });

        if (response.ok) {
            if (isLogin) {
                const data = await response.json();
                // СОХРАНЯЕМ ВСЕ ДАННЫЕ ОТ БЭКЕНДА
                localStorage.setItem('token', data.token);
                localStorage.setItem('userMail', data.mail);
                localStorage.setItem('role', data.role);
                localStorage.setItem('id', data.id);

                window.location.href = 'index.html';
            } else {
                msg.style.color = 'green';
                msg.innerText = 'Регистрация успешна! Теперь войдите.';
                setTimeout(() => toggleAuth(new Event('click')), 1500);
            }
        } else {
            msg.style.color = 'red';
            msg.innerText = 'Ошибка: ' + response.status;
        }
    } catch (e) {
        msg.style.color = 'red';
        msg.innerText = 'Сервер недоступен';
    }
}