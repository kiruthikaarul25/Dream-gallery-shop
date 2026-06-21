// ─── Photo Selection ────────────────────────────
document.querySelectorAll('.photo-card').forEach(card => {
    card.addEventListener('click', function() {
        this.classList.toggle('selected');
        const badge = this.querySelector('.check-badge');
        if (badge) {
            badge.style.display =
                this.classList.contains('selected')
                ? 'flex' : 'none';
        }
    });
});

// ─── No Screenshot / Download Protection ────────
document.addEventListener('contextmenu', e => e.preventDefault());
document.addEventListener('keydown', e => {
    if (e.key === 'PrintScreen' ||
        (e.ctrlKey && ['s','u','p'].includes(e.key.toLowerCase())) ||
        e.key === 'F12') {
        e.preventDefault();
    }
});