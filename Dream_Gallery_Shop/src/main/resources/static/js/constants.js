/**
 * ============================================================
 *  DREAM GALLERY — SITE CONSTANTS
 *  Edit only this file to update links/info across ALL pages
 * ============================================================
 */

const DG = {

  /* ── Brand ── */
  STUDIO_NAME    : "Dream Gallery Photography",
  INSTA_HANDLE   : "@dreamgalleryphoto",       // shown as text on pages
  LOCATION       : "Chennai · Tamil Nadu",

  /* ── Social Links ── */
  INSTA_URL      : "https://instagram.com/dreamgalleryphoto",
  WHATSAPP_NUMBER: "919876543210",              // country code + number, no + or spaces
  WHATSAPP_MSG   : "Hi! I'm interested in your photography services.",

  /* ── Contact ── */
  PHONE_1        : "+91 98765 43210",
  PHONE_2        : "+91 98765 43211",
  EMAIL          : "hello@dreamgallery.in",
  ADDRESS        : "123, Anna Nagar Main Road, Chennai — 600040",
  HOURS          : "Mon – Sat &nbsp;10 AM – 7 PM",
  HOURS_NOTE     : "Sunday by appointment",

  /* ── Bottom Nav ── */
  NAV: [
    { label: "Work",     href: "/work",          icon: "ti-video"   },
    { label: "Select",   href: "/select",         icon: "ti-photo"   },
    { label: "Packages", href: "/packages-page",  icon: "ti-diamond" },
    { label: "Contact",  href: "/contact",        icon: "ti-phone"   }
  ]
};

/* ═══════════════════════════════════════════════════════════
   AUTO-INJECT — runs on every page that includes this file
   ═══════════════════════════════════════════════════════════ */

(function () {

  /* ── 1. Fixed social icons (Instagram + WhatsApp) ── */
  const waURL = `https://wa.me/${DG.WHATSAPP_NUMBER}?text=${encodeURIComponent(DG.WHATSAPP_MSG)}`;

  document.querySelectorAll('.fixed-social').forEach(el => {
    el.innerHTML = `
      <a href="${DG.INSTA_URL}" target="_blank" aria-label="Instagram">
        <i class="ti ti-brand-instagram"></i>
      </a>
      <a href="${waURL}" target="_blank" aria-label="WhatsApp">
        <i class="ti ti-brand-whatsapp"></i>
      </a>`;
  });

  /* ── 2. Inline insta-link buttons (e.g. "View on Instagram") ── */
  document.querySelectorAll('a.insta-link').forEach(a => {
    a.href = DG.INSTA_URL;
  });

  /* ── 3. Instagram handle text (e.g. @dreamgalleryphoto) ── */
  document.querySelectorAll('.insta-handle').forEach(el => {
    el.textContent = DG.INSTA_HANDLE;
  });

  /* ── 4. Bottom nav — rebuild from DG.NAV ── */
  const currentPath = window.location.pathname;

  document.querySelectorAll('nav.bottom-nav').forEach(nav => {
    const ul = nav.querySelector('ul') || document.createElement('ul');
    ul.innerHTML = DG.NAV.map(item => {
      const isActive = currentPath === item.href || currentPath.startsWith(item.href + '/');
      return `<li>
        <a href="${item.href}"${isActive ? ' class="active"' : ''}>
          <i class="ti ${item.icon}"></i>
          <span>${item.label}</span>
        </a>
      </li>`;
    }).join('');
    if (!nav.querySelector('ul')) nav.appendChild(ul);
  });

  /* ── 5. Contact page dynamic values ── */
  const phoneLink1 = document.getElementById('dg-phone-1');
  const phoneLink2 = document.getElementById('dg-phone-2');
  const emailLink  = document.getElementById('dg-email');
  const addressEl  = document.getElementById('dg-address');
  const hoursEl    = document.getElementById('dg-hours');
  const hoursNote  = document.getElementById('dg-hours-note');
  const locationEl = document.getElementById('dg-location');
  const instaTxt   = document.getElementById('dg-insta-text');

  if (phoneLink1) { phoneLink1.href = `tel:${DG.PHONE_1.replace(/\s/g,'')}`;  phoneLink1.textContent = DG.PHONE_1; }
  if (phoneLink2) { phoneLink2.href = `tel:${DG.PHONE_2.replace(/\s/g,'')}`;  phoneLink2.textContent = DG.PHONE_2; }
  if (emailLink)  { emailLink.href  = `mailto:${DG.EMAIL}`;                    emailLink.textContent  = DG.EMAIL;  }
  if (addressEl)  { addressEl.textContent = DG.ADDRESS; }
  if (hoursEl)    { hoursEl.innerHTML     = DG.HOURS;   }
  if (hoursNote)  { hoursNote.textContent = DG.HOURS_NOTE; }
  if (locationEl) { locationEl.textContent = DG.LOCATION; }
  if (instaTxt)   { instaTxt.textContent   = DG.INSTA_HANDLE; }

})();
