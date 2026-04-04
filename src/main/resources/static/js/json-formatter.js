const textarea = document.getElementById('jsonInput');
const formatBtn = document.getElementById('formatBtn');
const clearBtn = document.getElementById('clearBtn');
const exampleBtn = document.getElementById('exampleBtn');
const errorDiv = document.getElementById('errorMsg');
const treeContainer = document.getElementById('jsonTreeContainer');

// Функция для отрисовки JSON в виде дерева
function renderJsonTree(jsonData, container) {
    if (!container) return;
    container.innerHTML = '';

    if (jsonData === undefined || jsonData === null) {
        container.innerHTML = '<i style="color:#888;">Нет данных</i>';
        return;
    }

    // Функция копирования текста в буфер
    function copyToClipboard(text, event) {
        event.stopPropagation();
        navigator.clipboard.writeText(text).then(() => {
            const btn = event.target;
            const originalText = btn.textContent;
            btn.textContent = '✓';
            setTimeout(() => { btn.textContent = originalText; }, 800);
        }).catch(() => alert('Не удалось скопировать'));
    }

    function createNode(value, key = null) {
        // Создаём основной контейнер узла
        const nodeDiv = document.createElement('div');
        nodeDiv.className = 'json-tree-node';

        const contentDiv = document.createElement('div');
        contentDiv.className = 'node-content';

        // Добавляем ключ, если есть
        if (key !== null) {
            const keySpan = document.createElement('span');
            keySpan.className = 'key-name';
            keySpan.textContent = key + ':';
            contentDiv.appendChild(keySpan);
        }

        // --- Обработка примитивов (null, string, number, boolean) ---
        if (value === null) {
            const iconSpan = document.createElement('span');
            iconSpan.textContent = '🚫 ';
            contentDiv.appendChild(iconSpan);
            const valueSpan = document.createElement('span');
            valueSpan.className = 'value-null';
            valueSpan.textContent = 'null';
            contentDiv.appendChild(valueSpan);
            const copyBtn = document.createElement('button');
            copyBtn.textContent = '📋';
            copyBtn.className = 'copy-btn';
            copyBtn.title = 'Копировать значение';
            copyBtn.onclick = (e) => copyToClipboard('null', e);
            contentDiv.appendChild(copyBtn);
            nodeDiv.appendChild(contentDiv);
            return nodeDiv;
        }

        if (typeof value !== 'object') {
            let icon = '';
            let displayValue = '';
            let copyValue = '';
            if (typeof value === 'string') {
                icon = '🔤 ';
                displayValue = `"${value}"`;
                copyValue = value;
            } else if (typeof value === 'number') {
                icon = '🔢 ';
                displayValue = value;
                copyValue = value;
            } else if (typeof value === 'boolean') {
                icon = value ? '✔️ ' : '❌ ';
                displayValue = value;
                copyValue = value;
            } else {
                icon = '📄 ';
                displayValue = value;
                copyValue = value;
            }
            const iconSpan = document.createElement('span');
            iconSpan.textContent = icon;
            contentDiv.appendChild(iconSpan);
            const valueSpan = document.createElement('span');
            if (typeof value === 'string') valueSpan.className = 'value-string';
            else if (typeof value === 'number') valueSpan.className = 'value-number';
            else if (typeof value === 'boolean') valueSpan.className = 'value-boolean';
            valueSpan.textContent = displayValue;
            contentDiv.appendChild(valueSpan);
            const copyBtn = document.createElement('button');
            copyBtn.textContent = '📋';
            copyBtn.className = 'copy-btn';
            copyBtn.title = 'Копировать значение';
            copyBtn.onclick = (e) => copyToClipboard(String(copyValue), e);
            contentDiv.appendChild(copyBtn);
            nodeDiv.appendChild(contentDiv);
            return nodeDiv;
        }

        // --- Объект или массив (папка) ---
        const isArray = Array.isArray(value);
        const keys = Object.keys(value);
        const isEmpty = keys.length === 0;

        // Переключатель (▼ / ▶)
        const toggleSpan = document.createElement('span');
        toggleSpan.className = 'toggle-icon';
        toggleSpan.textContent = isEmpty ? '  ' : '▼ ';
        if (!isEmpty) {
            toggleSpan.style.cursor = 'pointer';
        }

        // Иконка папки
        const folderIconSpan = document.createElement('span');
        folderIconSpan.className = 'folder-icon';
        folderIconSpan.textContent = '📁 ';

        contentDiv.appendChild(toggleSpan);
        contentDiv.appendChild(folderIconSpan);

        // Открывающая скобка
        const openBracket = document.createElement('span');
        openBracket.className = 'bracket';
        openBracket.textContent = isArray ? '[' : '{';
        contentDiv.appendChild(openBracket);

        // Многоточие (будет видно только в свёрнутом состоянии)
        let ellipsisSpan = null;
        if (!isEmpty) {
            ellipsisSpan = document.createElement('span');
            ellipsisSpan.textContent = ' … ';
            ellipsisSpan.style.color = '#888';
            contentDiv.appendChild(ellipsisSpan);
        }

        const closeBracket = document.createElement('span');
        closeBracket.className = 'bracket';
        closeBracket.textContent = isArray ? ']' : '}';
        contentDiv.appendChild(closeBracket);

        nodeDiv.appendChild(contentDiv);

        // Контейнер для дочерних элементов
        const childrenContainer = document.createElement('div');
        childrenContainer.className = 'children-container';
        childrenContainer.style.display = 'block';

        // Рекурсивно добавляем дочерние элементы
        keys.forEach((k, idx) => {
            const childValue = value[k];
            let childKey = isArray ? null : k;
            const childNode = createNode(childValue, childKey);

            // Для массивов добавляем индекс перед содержимым
            if (isArray) {
                const indexSpan = document.createElement('span');
                indexSpan.style.color = '#888';
                indexSpan.style.minWidth = '30px';
                indexSpan.textContent = `${idx}: `;
                // Вставляем индекс в начало .node-content
                const childContent = childNode.querySelector('.node-content');
                if (childContent) {
                    childContent.insertBefore(indexSpan, childContent.firstChild);
                }
            }
            childrenContainer.appendChild(childNode);
        });

        nodeDiv.appendChild(childrenContainer);

        // Логика сворачивания/разворачивания
        if (!isEmpty) {
            let expanded = true;
            toggleSpan.addEventListener('click', (e) => {
                e.stopPropagation();
                expanded = !expanded;
                childrenContainer.style.display = expanded ? 'block' : 'none';
                toggleSpan.textContent = expanded ? '▼ ' : '▶ ';
                folderIconSpan.textContent = expanded ? '📂 ' : '📁 ';
                if (ellipsisSpan) {
                    ellipsisSpan.style.display = expanded ? 'none' : 'inline';
                }
            });
            // Скрываем многоточие по умолчанию (развёрнуто)
            if (ellipsisSpan) ellipsisSpan.style.display = 'none';
        }

        return nodeDiv;
    }

    const rootNode = createNode(jsonData);
    container.appendChild(rootNode);
}

// Инициализация кнопок после загрузки DOM
document.addEventListener('DOMContentLoaded', () => {
    if (formatBtn) {
        formatBtn.addEventListener('click', () => {
            const raw = textarea.value;
            try {
                const obj = JSON.parse(raw);
                const pretty = JSON.stringify(obj, null, 2);
                textarea.value = pretty;
                renderJsonTree(obj, treeContainer);
                if (errorDiv) errorDiv.innerText = '';
            } catch (e) {
                if (treeContainer) treeContainer.innerHTML = `<span style="color:#ff4c4c;">Ошибка: ${e.message}</span>`;
                if (errorDiv) errorDiv.innerText = e.message;
            }
        });
    }

    if (clearBtn) {
        clearBtn.addEventListener('click', () => {
            textarea.value = '';
            if (treeContainer) treeContainer.innerHTML = '<i style="color: #888;">Очищено. Введите JSON и нажмите "Отформатировать".</i>';
            if (errorDiv) errorDiv.innerText = '';
        });
    }

    if (exampleBtn) {
        exampleBtn.addEventListener('click', () => {
            const example = {
                "universe": "QA Universe",
                "version": 1.0,
                "features": ["API Testing", "UI Testing", "JSON Formatter"],
                "config": { "darkMode": true, "timeout": 30, "plugins": ["RestAssured", "Selenide"] },
                "nullValue": null,
                "isActive": true
            };
            const prettyExample = JSON.stringify(example, null, 2);
            textarea.value = prettyExample;
            renderJsonTree(example, treeContainer);
            if (errorDiv) errorDiv.innerText = '';
        });
    }
});


// Кнопка сворачивания/разворачивания левой панели (находится в правой панели)
const toggleBtn = document.getElementById('togglePanelBtn');
const inputPanel = document.querySelector('.input-panel');
const editorArea = document.querySelector('.editor-area');

if (toggleBtn && inputPanel && editorArea) {
    toggleBtn.addEventListener('click', () => {
        inputPanel.classList.toggle('collapsed');
        editorArea.classList.toggle('left-collapsed');
        const isCollapsed = inputPanel.classList.contains('collapsed');
        toggleBtn.textContent = isCollapsed ? '▶ Показать редактор JSON' : '◀ Скрыть редактор JSON';
    });
}