document.addEventListener('DOMContentLoaded', () => {
    console.log('QA University Lab loaded');

    const app = {
        apiUrl: '/api/notes',
        sidebarElement: document.getElementById('sidebarContent'),
        contentArea: document.getElementById('contentArea'),
        noteContent: document.getElementById('noteContent'),
        activeLink: null,

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
                        this.selectNote(note, link);
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

        selectNote(note, linkElement) {
            this.setActiveLink(linkElement);
            this.loadNote(note);
        },

        setActiveLink(linkElement) {
            if (this.activeLink) {
                this.activeLink.classList.remove('active');
            }
            this.activeLink = linkElement;
            if (this.activeLink) {
                this.activeLink.classList.add('active');
            }
        },

        async loadNote(note) {
            this.renderNoteState('loading', 'Загрузка заметки...');

            try {
                const apiPath = this.buildNoteApiPath(note.path);
                const response = await fetch(apiPath);

                if (response.status === 404) {
                    this.renderNoteState('error', 'Note not found.');
                    return;
                }

                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }

                const data = await response.json();
                this.renderNote(data);
            } catch (error) {
                console.error('Failed to load note:', error);
                this.renderNoteState('error', 'Failed to load note.<br>Please try again later.');
            }
        },

        buildNoteApiPath(path) {
            const normalizedPath = path.replace(/\\/g, '/');
            const firstSlashIndex = normalizedPath.indexOf('/');

            let category;
            let fileName;

            if (firstSlashIndex === -1) {
                category = '';
                fileName = normalizedPath;
            } else {
                category = normalizedPath.substring(0, firstSlashIndex);
                fileName = normalizedPath.substring(firstSlashIndex + 1);
            }

            const noteName = fileName.replace(/\.md$/i, '');
            return `/api/notes/${encodeURIComponent(category)}/${encodeURIComponent(noteName)}`;
        },

        renderNoteState(type, message) {
            this.noteContent.className = 'note-content';
            this.noteContent.innerHTML = `<p class="note-state ${type}-state">${message}</p>`;
            this.scrollContentToTop();
        },

        renderNote(note) {
            this.noteContent.className = 'note-content';
            // HTML приходит от backend (Markdown → HTML) и отображается как рендеренный контент.
            // Первый <h1> в content уже содержит заголовок заметки, поэтому отдельный header не рисуем.
            this.noteContent.innerHTML = `<div class="note-body">${note.content || ''}</div>`;
            this.scrollContentToTop();
        },

        scrollContentToTop() {
            if (this.contentArea) {
                this.contentArea.scrollTo({ top: 0, behavior: 'smooth' });
            }
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    };

    app.init();
});
