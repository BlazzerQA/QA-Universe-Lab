document.addEventListener('DOMContentLoaded', () => {
    console.log('QA University Lab loaded');

    const app = {
        apiBaseUrl: '/api/notes',
        contentElement: document.getElementById('noteContent'),

        init() {
            this.bindCategoryLinks();
        },

        bindCategoryLinks() {
            document.querySelectorAll('.category-list a').forEach(link => {
                link.addEventListener('click', (event) => {
                    event.preventDefault();
                    const category = link.getAttribute('data-category');
                    this.showPlaceholder(category);
                });
            });
        },

        showPlaceholder(category) {
            if (this.contentElement) {
                this.contentElement.innerHTML = `<p>Категория «${category}». Загрузка статей будет реализована позже.</p>`;
            }
        }
    };

    app.init();
});
