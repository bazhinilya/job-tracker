import { type Context } from 'telegraf';

// Определяем интерфейс для отчёта
export interface DailyReport {
  date: string | undefined;
  tasks: Record<string, number>; // { leetcode: 3, applications: 2 }
  mood?: 'great' | 'good' | 'neutral' | 'bad' | 'terrible';
  notes?: string;
}

// Простой тип для сессии
export interface UserSession {
  userId?: number;
  state?: string;
  step?: number;
  report?: DailyReport; // Добавляем свойство report
  waitingFor?: string; // Какое значение ожидаем от пользователя
}

// Расширяем контекст Telegraf
export interface BotContext extends Context {
  session?: UserSession;
}
