document.addEventListener('DOMContentLoaded', () => {
    console.log('QA University Lab loaded');

    const app = {
        apiUrl: '/api/notes',
        sidebarElement: document.getElementById('sidebarContent'),
        contentElement: document.getElementById('noteContent'),

        init() {
            this.loadNotes();
        },

        async loadNotes() {
            try {
                const response = await fetch(this.apiUrl);

                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }

                const notes = await response.json();

                if (!Array.isArray(notes)) {
                    throw new Error('Unexpected API response format');
                }

                if (notes.length === 0) {
                    this.renderEmptyState();
                    return;
                }

                this.renderSidebar(this.groupByCategory(notes));
            } catch (error) {
                console.error('Failed to load notes:', error);
                this.renderErrorState();
            }
        },

        groupByCategory(notes) {
            const grouped = notes.reduce((acc, note) => {
                const category = note.category || 'Uncategorized';
                if (!acc[category]) {
                    acc[category] = [];
                }
                acc[category].push(note);
                return acc;
            }, {});

            return Object.keys(grouped)
                .sort((a, b) => a.localeCompare(b))
                .reduce((acc, category) => {
                    acc[category] = grouped[category].sort((a, b) =>
                        a.title.localeCompare(b.title)
                    );
                    return acc;
                }, {});
        },

        renderSidebar(groupedNotes) {
            const fragment = document.createDocumentFragment();

            Object.entries(groupedNotes).forEach(([category, notes]) => {
                const categorySection = document.createElement('section');
                categorySection.className = 'category-section';

                const heading = document.createElement('h3');
                heading.className = 'category-title';
                heading.textContent = category.toUpperCase();
                categorySection.appendChild(heading);

                const list = document.createElement('ul');
                list.className = 'note-list';

                notes.forEach(note => {
                    const item = document.createElement('li');
                    const link = document.createElement('a');
                    link.href = '#';
                    link.className = 'note-link';
                    link.setAttribute('data-path', note.path);
                    link.setAttribute('data-title', note.title);
                    link.textContent = note.title;
                    link.addEventListener('click', (event) => {
                        event.preventDefault();
                        this.selectNote(note);
                    });
                    item.appendChild(link);
                    list.appendChild(item);
                });

                categorySection.appendChild(list);
                fragment.appendChild(categorySection);
            });

            this.sidebarElement.innerHTML = '';
            this.sidebarElement.appendChild(fragment);
        },

        renderEmptyState() {
            this.sidebarElement.innerHTML = '<p class="state-message empty-state">No notes available.</p>';
        },

        renderErrorState() {
            this.sidebarElement.innerHTML =
                '<p class="state-message error-state">Failed to load notes.<br>Please try again later.</p>';
        },

        selectNote(note) {
            if (this.contentElement) {
                this.contentElement.innerHTML =
                    `<p>Выбрана заметка: <strong>${note.title}</strong> (${note.path})</p>`;
            }
        }
    };

    app.init();
});
