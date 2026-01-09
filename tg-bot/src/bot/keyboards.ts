import { Markup } from 'telegraf';

// Главная клавиатура
export const mainKeyboard = Markup.keyboard([
  ['📊 Отчёт за сегодня', '📈 Статистика'],
  ['🎯 Мой план', '💼 Вакансии'],
  ['🆘 Помощь'],
]).resize();

// Клавиатура для отчёта
export const reportKeyboard = Markup.keyboard([
  ['✅ LeetCode', '📨 Отклики'],
  ['📚 Изучение', '💼 Разбор'],
  ['📤 Завершить отчёт', '🔙 Назад'],
]).resize();

// Inline клавиатура для выбора уровня
export const levelKeyboard = Markup.inlineKeyboard([
  [
    Markup.button.callback('👶 Junior', 'level_junior'),
    Markup.button.callback('👨‍💻 Middle', 'level_middle'),
  ],
  [
    Markup.button.callback('👴 Senior', 'level_senior'),
  ],
]);

// Inline клавиатура для настроек
export const setupKeyboard = Markup.inlineKeyboard([
  [
    Markup.button.callback('🎯 Цели', 'setup_goals'),
    Markup.button.callback('🛠️ Технологии', 'setup_tech'),
  ],
  [
    Markup.button.callback('📅 Расписание', 'setup_schedule'),
    Markup.button.callback('📊 Метрики', 'setup_metrics'),
  ],
  [
    Markup.button.callback('✅ Завершить настройку', 'setup_complete'),
  ],
]);
