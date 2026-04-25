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

