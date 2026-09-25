/**
 * RAILCONNECT — Frontend Authentication & UI Session Manager
 */

const Auth = {
  initNavbar() {
    const user = Api.getUser();
    const navActions = document.querySelector('.nav-actions');
    if (!navActions) return;

    if (user) {
      const roleBadge = user.role.replace('ROLE_', '');
      navActions.innerHTML = `
        <div style="display: flex; align-items: center; gap: 10px;">
          <span style="font-size: 0.85rem; font-weight: 600; color: var(--primary);">
            👤 ${user.fullName || user.username}
            <span class="badge ${user.role === 'ROLE_ADMIN' ? 'badge-danger' : user.role === 'ROLE_INSPECTOR' ? 'badge-warning' : 'badge-info'}" style="margin-left: 4px;">
              ${roleBadge}
            </span>
          </span>
          <button class="btn btn-secondary btn-sm" onclick="Auth.logout()">Logout</button>
        </div>
        <select class="lang-select" aria-label="Select Language">
          <option value="en">English (EN)</option>
          <option value="hi">हिन्दी (HI)</option>
          <option value="te">తెలుగు (TE)</option>
          <option value="ta">தமிழ் (TA)</option>
        </select>
      `;
    } else {
      navActions.innerHTML = `
        <a href="login.html" class="btn btn-secondary btn-sm" data-i18n="nav.login">Login</a>
        <a href="register.html" class="btn btn-primary btn-sm" data-i18n="nav.register">Register</a>
        <select class="lang-select" aria-label="Select Language">
          <option value="en">English (EN)</option>
          <option value="hi">हिन्दी (HI)</option>
          <option value="te">తెలుగు (TE)</option>
          <option value="ta">தமிழ் (TA)</option>
        </select>
      `;
    }

    if (window.I18n) {
      window.I18n.bindLanguageSwitcher();
      window.I18n.applyTranslations();
    }
  },

  logout() {
    Api.logout();
  },

  showToast(message, type = 'info') {
    let container = document.querySelector('.toast-container');
    if (!container) {
      container = document.createElement('div');
      container.className = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.style.borderLeft = type === 'success' ? '4px solid var(--accent-green)' :
                             type === 'danger' ? '4px solid var(--danger)' :
                             type === 'warning' ? '4px solid var(--accent-saffron)' :
                             '4px solid var(--primary)';
    toast.innerHTML = `
      <span>${type === 'success' ? '✅' : type === 'danger' ? '⚠️' : 'ℹ️'}</span>
      <span>${message}</span>
    `;

    container.appendChild(toast);
    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transition = 'opacity 0.3s ease';
      setTimeout(() => toast.remove(), 300);
    }, 4000);
  }
};

window.Auth = Auth;
document.addEventListener('DOMContentLoaded', () => Auth.initNavbar());
