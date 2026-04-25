document.addEventListener('DOMContentLoaded', () => {
    const bgContainer = document.querySelector('.floating-background');
    if (!bgContainer) return;

    // Список иконок (можно использовать любые эмодзи или HTML-символы)
    const icons = ['☕', '☕', '🐞', '🐞', '🚀', '🚀', '💻', '💻', '🧪', '🧪', '🔄', '🔄', '🌐', '🌐'];

    const numIcons = 30; // Количество плавающих элементов

    for (let i = 0; i < numIcons; i++) {
        const icon = document.createElement('div');
        icon.className = 'floating-icon';
        // Случайный выбор иконки из массива
        icon.textContent = icons[Math.floor(Math.random() * icons.length)];

        // Случайные параметры для позиции и анимации
        const size = Math.floor(Math.random() * 30) + 20; // размер от 20 до 50px
        const left = Math.random() * 100; // позиция по ширине экрана
        const duration = Math.random() * 15 + 8; // длительность от 8 до 23 секунд
        const delay = Math.random() * 6; // задержка перед стартом
        const opacity = Math.random() * 0.15 + 0.05; // прозрачность

        icon.style.fontSize = `${size}px`;
        icon.style.left = `${left}%`;
        icon.style.animationDuration = `${duration}s`;
        icon.style.animationDelay = `${delay}s`;
        icon.style.opacity = opacity;

        bgContainer.appendChild(icon);
    }
});

