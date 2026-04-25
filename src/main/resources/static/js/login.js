// ==================== ПЕРЕКЛЮЧЕНИЕ ВКЛАДОК ====================
const loginTab = document.getElementById('loginTabBtn');
const registerTab = document.getElementById('registerTabBtn');
const loginForm = document.getElementById('loginForm');
const registerFormDiv = document.getElementById('registerForm');

function switchToLoginTab() {
    loginTab.classList.add('active');
    registerTab.classList.remove('active');
    loginForm.style.display = 'block';
    registerFormDiv.style.display = 'none';
}

function switchToRegisterTab() {
    registerTab.classList.add('active');
    loginTab.classList.remove('active');
    loginForm.style.display = 'none';
    registerFormDiv.style.display = 'block';
}

loginTab.addEventListener('click', switchToLoginTab);
registerTab.addEventListener('click', switchToRegisterTab);

// ==================== ЛОГИКА РЕГИСТРАЦИИ ====================
const regPhone = document.getElementById('regPhone');
const regPassword = document.getElementById('regPassword');
const regConfirm = document.getElementById('regConfirmPassword');
const registerBtn = document.getElementById('registerBtn');
const registerMessage = document.getElementById('registerMessage');
const registerError = document.getElementById('registerError');
const phoneError = document.getElementById('regPhoneError');
const passwordError = document.getElementById('regPasswordError');
const confirmError = document.getElementById('regConfirmError');

function validateForm() {
    let isValid = true;
    phoneError.style.display = 'none';
    passwordError.style.display = 'none';
    confirmError.style.display = 'none';

    const phoneRegex = /^\+7\d{10}$/;
    if (!phoneRegex.test(regPhone.value)) {
        phoneError.textContent = 'Неверный формат. Используйте +7XXXXXXXXXX';
        phoneError.style.display = 'block';
        isValid = false;
    }

    if (regPassword.value.length < 6) {
        passwordError.textContent = 'Пароль должен содержать минимум 6 символов';
        passwordError.style.display = 'block';
        isValid = false;
    }

    if (regPassword.value !== regConfirm.value) {
        confirmError.textContent = 'Пароли не совпадают';
        confirmError.style.display = 'block';
        isValid = false;
    }

    return isValid;
}

async function handleRegistration() {
    registerMessage.style.display = 'none';
    registerError.style.display = 'none';

    if (!validateForm()) {
        return;
    }

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                phone: regPhone.value,
                password: regPassword.value
            })
        });

        const data = await response.json();

        if (response.status === 201) {
            registerMessage.textContent = '✅ Регистрация успешна! Теперь войдите.';
            registerMessage.style.display = 'block';

            // Автоматически подставляем телефон в форму логина
            const loginPhoneInput = document.querySelector('input[name="phone"]');
            if (loginPhoneInput) {
                loginPhoneInput.value = regPhone.value;
            }

            // Переключаемся на вкладку входа через 1 секунду
            setTimeout(() => {
                switchToLoginTab();
            }, 1000);

            // Очищаем форму регистрации
            regPhone.value = '';
            regPassword.value = '';
            regConfirm.value = '';

        } else if (response.status === 400) {
            
            let errorMessage = 'Проверьте правильность заполнения полей';

            if (data && data.errors && Array.isArray(data.errors) && data.errors.length > 0) {
                const firstError = data.errors[0];
                if (firstError.defaultMessage) {
                    errorMessage = firstError.defaultMessage;
                } else if (firstError.message) {
                    errorMessage = firstError.message;
                }
            } else if (data && data.message && typeof data.message === 'string') {
                errorMessage = data.message;
            } else if (data && typeof data === 'string') {
                errorMessage = data;
            }

            registerError.textContent = errorMessage;
            registerError.style.display = 'block';

        } else if (response.status === 409) {
            registerError.textContent = data.error || 'Пользователь с таким телефоном уже существует';
            registerError.style.display = 'block';
        } else if (response.status === 400) {
            const errorMessage = data.message || data.error || 'Ошибка валидации';
            registerError.textContent = errorMessage;
            registerError.style.display = 'block';
        } else {
            registerError.textContent = 'Ошибка при регистрации. Попробуйте позже.';
            registerError.style.display = 'block';
        }
    } catch (error) {
        registerError.textContent = 'Ошибка сети. Проверьте подключение.';
        registerError.style.display = 'block';
    }
}

registerBtn.addEventListener('click', handleRegistration);