(() => {
    const dropdown = document.querySelector(".dropdown");
    if (!dropdown) return;

    const button = dropdown.querySelector(".dropbtn");
    const menu = dropdown.querySelector(".dropdown-content");
    if (!button || !menu) return;

    const close = () => dropdown.classList.remove("open");
    const toggle = () => dropdown.classList.toggle("open");

    button.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        toggle();
    });

    menu.addEventListener("click", (e) => {
        e.stopPropagation();
    });

    document.addEventListener("click", (e) => {
        if (!dropdown.contains(e.target)) {
            close();
        }
    });

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") close();
    });
})();

// Обработка вирусной кнопки на главной странице
document.addEventListener('DOMContentLoaded', function() {
    const virusBtn = document.getElementById('virusBtn');
    const scaryOverlay = document.getElementById('scaryOverlay');
    
    if (virusBtn && scaryOverlay) {
        virusBtn.addEventListener('click', function(e) {
            e.preventDefault();
            
            scaryOverlay.style.display = 'flex';
            
            setTimeout(() => {
                window.location.href = 'https://avatars.mds.yandex.net/i?id=7072d2699522c002261da3c214a7b288_l-10638736-images-thumbs&n=13';
            }, 3000);
        });
    }
});
