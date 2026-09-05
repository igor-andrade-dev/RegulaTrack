import { Injectable } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly storageKey = 'regulatrack-theme';

  constructor() {
    this.apply(this.read());
  }

  get isDark(): boolean {
    return document.documentElement.dataset['theme'] === 'dark';
  }

  toggle(): void {
    this.apply(this.isDark ? 'light' : 'dark');
  }

  private read(): Theme {
    const value = localStorage.getItem(this.storageKey);
    return value === 'dark' ? 'dark' : 'light';
  }

  private apply(theme: Theme): void {
    document.documentElement.dataset['theme'] = theme;
    localStorage.setItem(this.storageKey, theme);
  }
}
