/**
 * RAILCONNECT — Multilingual Engine (en, hi, te, ta)
 */

const I18n = {
  currentLang: localStorage.getItem('railconnect_lang') || 'en',
  translations: {},

  async init() {
    await this.setLanguage(this.currentLang);
    this.bindLanguageSwitcher();
  },

  async setLanguage(lang) {
    try {
      const res = await fetch(`i18n/${lang}.json`);
      if (!res.ok) throw new Error(`Could not load ${lang}.json`);
      this.translations = await res.json();
      this.currentLang = lang;
      localStorage.setItem('railconnect_lang', lang);
      this.applyTranslations();

      // Update selector UI if present
      document.querySelectorAll('.lang-select').forEach(select => {
        select.value = lang;
      });
    } catch (e) {
      console.warn('[I18n Load Error]:', e);
    }
  },

  applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
      const key = el.getAttribute('data-i18n');
      const val = this.getNestedValue(this.translations, key);
      if (val) {
        if (el.tagName === 'INPUT' && el.getAttribute('placeholder')) {
          el.setAttribute('placeholder', val);
        } else {
          el.textContent = val;
        }
      }
    });
  },

  getNestedValue(obj, keyPath) {
    return keyPath.split('.').reduce((acc, part) => acc && acc[part], obj);
  },

  bindLanguageSwitcher() {
    document.querySelectorAll('.lang-select').forEach(select => {
      select.value = this.currentLang;
      select.addEventListener('change', (e) => {
        this.setLanguage(e.target.value);
      });
    });
  }
};

window.I18n = I18n;
document.addEventListener('DOMContentLoaded', () => I18n.init());
