(function() {
    const canvas = document.getElementById('codeCanvas');
    if (!canvas) return; // если canvas нет на странице – выходим

    const ctx = canvas.getContext('2d');
    let width, height;
    let columns = [];
    const words = [
        'JAVA', 'SPRING', 'JUNIT', 'MOCKITO', 'RESTASSURED',
        'TEST', 'QA', 'CODE', 'AUTOMATION', 'API', 'JSON',
        '{}', '()', '->', ';', 'class', 'public', 'void'
    ];
    const fontSize = 20;
    let drops = [];

    function resize() {
        width = window.innerWidth;
        height = window.innerHeight;
        canvas.width = width;
        canvas.height = height;
        columns = Math.floor(width / fontSize);
        drops = [];
        for (let i = 0; i < columns; i++) {
            drops[i] = Math.random() * -height;
        }
    }

    function draw() {
        ctx.fillStyle = 'rgba(18, 18, 18, 0.1)';
        ctx.fillRect(0, 0, width, height);
        ctx.fillStyle = '#33ff55';
        ctx.font = fontSize + 'px monospace';
        for (let i = 0; i < drops.length; i++) {
            const word = words[Math.floor(Math.random() * words.length)];
            const x = i * fontSize;
            const y = drops[i] * fontSize;
            ctx.fillText(word, x, y);
            if (y > height && Math.random() > 0.975) {
                drops[i] = 0;
            }
            drops[i] += 0.6;
        }
    }

    function animate() {
        draw();
        requestAnimationFrame(animate);
    }

    window.addEventListener('resize', resize);
    resize();
    animate();
})();