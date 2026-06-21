// ============================================
// Dream Gallery Photography — Photo Protection
// ============================================

(function () {

    // Right click disable
    document.addEventListener('contextmenu', e => e.preventDefault());

    // Keyboard shortcuts disable
    document.addEventListener('keydown', function (e) {
        // PrintScreen
        if (e.key === 'PrintScreen') {
            e.preventDefault();
            navigator.clipboard?.writeText('');
        }
        // Ctrl+S, Ctrl+U, Ctrl+P, Ctrl+A, Ctrl+C
        if (e.ctrlKey && ['s','u','p','a','c'].includes(e.key.toLowerCase())) {
            e.preventDefault();
        }
        // F12, F5 (devtools)
        if (['F12'].includes(e.key)) {
            e.preventDefault();
        }
        // Ctrl+Shift+I, Ctrl+Shift+J, Ctrl+Shift+C
        if (e.ctrlKey && e.shiftKey && ['i','j','c'].includes(e.key.toLowerCase())) {
            e.preventDefault();
        }
    });

    // Drag disable
    document.addEventListener('dragstart', e => e.preventDefault());

    // Mobile long press disable
    document.addEventListener('touchstart', function (e) {
        if (e.touches.length > 1) e.preventDefault();
    }, { passive: false });

    // CSS protection on all images
    const style = document.createElement('style');
    style.textContent = `
        img {
            user-select: none !important;
            -webkit-user-select: none !important;
            -webkit-user-drag: none !important;
            pointer-events: none !important;
            -webkit-touch-callout: none !important;
        }
        * {
            user-select: none !important;
            -webkit-user-select: none !important;
        }
        input, textarea {
            user-select: text !important;
            -webkit-user-select: text !important;
        }
    `;
    document.head.appendChild(style);

    // Transparent overlay on images
    document.querySelectorAll('img').forEach(img => {
        const wrapper = img.parentElement;
        if (wrapper) {
            wrapper.style.position = 'relative';
            const overlay = document.createElement('div');
            overlay.style.cssText = `
                position:absolute;inset:0;
                z-index:10;cursor:default;
                background:transparent;
            `;
            wrapper.appendChild(overlay);
        }
    });

    // DevTools detect (basic)
    const devtools = { open: false };
    setInterval(() => {
        const threshold = 160;
        if (window.outerWidth - window.innerWidth > threshold ||
            window.outerHeight - window.innerHeight > threshold) {
            if (!devtools.open) {
                devtools.open = true;
                document.body.innerHTML = `
                    <div style="display:flex;align-items:center;
                    justify-content:center;height:100vh;
                    background:#0a0a0a;color:#D4AF37;
                    font-family:Poppins,sans-serif;text-align:center;">
                        <div>
                            <div style="font-size:3rem">🔒</div>
                            <h2 style="margin:16px 0 8px">Access Restricted</h2>
                            <p style="color:#888">Please close developer tools to continue.</p>
                        </div>
                    </div>`;
            }
        } else {
            devtools.open = false;
        }
    }, 1000);

})();