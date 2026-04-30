// Обработка страницы управления товарами
document.addEventListener('DOMContentLoaded', function() {
    loadProducts();
    
    // Обработка формы добавления продукта
    document.getElementById('addProductForm').addEventListener('submit', function(e) {
        e.preventDefault();
        addProduct();
    });
});

// Загрузка продуктов
async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        showNotification('Ошибка загрузки продуктов: ' + error.message, 'error');
    }
}

// Отображение продуктов
function displayProducts(products) {
    const container = document.getElementById('productsList');
    
    if (products.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #aaa;">Нет продуктов для отображения</p>';
        return;
    }

    // Используем карточки вместо таблицы
    container.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-details">
                <div class="product-name">${product.productName}</div>
                <div class="product-price">Цена: ${product.price.toFixed(2)} ₽</div>
                <div class="product-id">ID: ${product.productId}</div>
            </div>
            <div class="product-actions">
                <button onclick="deleteProduct('${product.productId}')" class="btn btn-danger">Удалить</button>
            </div>
        </div>
    `).join('');
}

// Добавление нового продукта
async function addProduct() {
    const nameInput = document.getElementById('productName');
    const priceInput = document.getElementById('price');

    const productName = nameInput.value.trim();
    const price = parseFloat(priceInput.value);

    if (!productName) {
        showNotification('Название продукта не может быть пустым', 'error');
        return;
    }

    if (isNaN(price) || price <= 0) {
        showNotification('Цена должна быть положительным числом', 'error');
        return;
    }

    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                productName: productName,
                price: price
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
        }

        // Очищаем форму
        nameInput.value = '';
        priceInput.value = '';

        // Обновляем список продуктов
        await loadProducts();
        showNotification('Продукт успешно добавлен', 'success');
    } catch (error) {
        showNotification('Ошибка добавления продукта: ' + error.message, 'error');
    }
}

// Удаление продукта
async function deleteProduct(productId) {
    if (!confirm('Вы уверены, что хотите удалить этот продукт?')) {
        return;
    }

    try {
        const response = await fetch(`/api/products/${productId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Продукт не найден');
            } else {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }

        // Обновляем список продуктов
        await loadProducts();
        showNotification('Продукт успешно удален', 'success');
    } catch (error) {
        showNotification('Ошибка удаления продукта: ' + error.message, 'error');
    }
}

// Отображение уведомлений
function showNotification(message, type) {
    const notification = document.getElementById('notification');
    notification.innerHTML = `<div class="notification ${type}">${message}</div>`;
    
    // Автоматическое скрытие уведомления через 5 секунд
    setTimeout(() => {
        notification.innerHTML = '';
    }, 5000);
}