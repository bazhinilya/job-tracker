import { session, type Telegraf } from 'telegraf';
import { levelKeyboard, mainKeyboard, reportKeyboard } from './keyboards';
import { type BotContext } from './types';
import { USER_STATES } from './commands';
import { logger } from '@core/logger';

// Форматирование сообщений
const formatWelcomeMessage = (firstName: string) => `
🎯 *Добро пожаловать в Job Quest Tracker, ${firstName}*

Я буду твоим личным тренером по подготовке к собеседованиям.

*Основные команды:*
/start - Начать работу
/help - Помощь и инструкции
/plan - Настроить план подготовки
/report - Отправить ежедневный отчёт
/stats - Посмотреть статистику

Готов начать путь к новой работе? 🚀
`;

const formatHelpMessage = () => `
🆘 *Помощь по использованию бота*

*Основные команды:*
/start - Начать работу с ботом
/help - Эта справка
/plan - Настройка плана
/report - Ежедневный отчёт
/stats - Статистика

*Как это работает:*
1. Настрой план через /plan
2. Ежедневно отправляй отчёт через /report
3. Следи за прогрессом через /stats
`;

export const setupHandlers = (bot: Telegraf<BotContext>) => {
  // Настройка сессии
  bot.use(session({
    defaultSession: () => ({
      state: USER_STATES.IDLE,
    }),
  }));

  // Команда /start
  bot.start(async (ctx) => {
    const firstName = ctx.from.first_name || 'друг';

    logger.info(`User ${ctx.from.id} started bot`);

    await ctx.replyWithMarkdown(
      formatWelcomeMessage(firstName),
      mainKeyboard,
    );

    // Инициализируем сессию
    ctx.session = {
      userId: ctx.from.id,
      state: USER_STATES.IDLE,
    };
  });

  // Команда /help
  bot.help(async (ctx) => {
    await ctx.replyWithMarkdown(
      formatHelpMessage(),
      mainKeyboard,
    );
  });

  // Команда /plan
  bot.command('plan', async (ctx) => {
    await ctx.replyWithMarkdown(
      '📋 *Настройка плана подготовки*\n\n' +
      'Выбери свой уровень:',
      levelKeyboard,
    );

    // Обновляем состояние
    ctx.session = {
      ...ctx.session,
      state: USER_STATES.PLAN_SETUP,
      step: 1,
    };
  });

  // Команда /report
  bot.command('report', async (ctx) => {
    await ctx.reply(
      '📊 *Ежедневный отчёт*\n\n' +
      'Выбери тип задачи для добавления:',
      {
        parse_mode: 'Markdown',
        ...reportKeyboard,
      },
    );

    // Обновляем состояние для отчёта
    ctx.session = {
      ...ctx.session,
      state: USER_STATES.REPORTING,
      report: {
        date: new Date().toISOString().split('T')[0],
        tasks: {},
      },
    };
  });

  // Команда /stats
  bot.command('stats', async (ctx) => {
    await ctx.replyWithMarkdown(
      '📈 *Статистика*\n\n' +
      'Эта функция скоро будет доступна!\n' +
      'Здесь ты увидишь:\n' +
      '• Выполнение плана\n' +
      '• Задачи LeetCode\n' +
      '• Отправленные отклики\n' +
      '• Часы изучения\n\n' +
      '*Ожидай обновления!* 🚀',
      mainKeyboard,
    );
  });

  // Обработка кнопок отчёта
  bot.hears('📊 Отчёт за сегодня', async (ctx) => {
    await ctx.reply(
      'Используй команду /report для отправки ежедневного отчёта!',
      mainKeyboard,
    );
  });

  bot.hears('✅ LeetCode', async (ctx) => {
    if (ctx.session?.state === USER_STATES.REPORTING) {
      await ctx.reply('Сколько задач LeetCode решил сегодня?\nОтправь число:');
      ctx.session.waitingFor = 'leetcode';
    }
  });

  bot.hears('📤 Завершить отчёт', async (ctx) => {
    if (ctx.session?.state === USER_STATES.REPORTING) {
      const report = ctx.session.report;
      const tasks = report?.tasks || {};

      let message = '✅ *Отчёт сохранён!*\n\n';

      if (tasks.leetcode) {
        message += `• LeetCode: ${tasks.leetcode} задач\n`;
      }
      if (tasks.applications) {
        message += `• Откликов: ${tasks.applications}\n`;
      }

      if (Object.keys(tasks).length === 0) {
        message = '❌ Отчёт пустой. Добавь хотя бы одну задачу.';
      } else {
        message += '\nОтличная работа! Продолжай в том же духе! 👏';
      }

      await ctx.reply(message, {
        parse_mode: 'Markdown',
        ...mainKeyboard,
      });

      // Сбрасываем состояние
      ctx.session.state = USER_STATES.IDLE;
      delete ctx.session.report;
      delete ctx.session.waitingFor;
    }
  });

  // Обработка числового ввода для отчёта
  bot.on('text', async (ctx) => {
    if (
      ctx.session?.state === USER_STATES.REPORTING &&
      ctx.session.waitingFor &&
      ctx.message.text
    ) {
      const text = ctx.message.text;
      const number = parseInt(text, 10);

      if (isNaN(number) || number < 0) {
        await ctx.reply('Пожалуйста, отправь корректное число');
        return;
      }

      const taskType = ctx.session.waitingFor;
      ctx.session.report!.tasks[taskType] = number;
      delete ctx.session.waitingFor;

      await ctx.reply(
        `✅ Добавлено: ${number} ${getTaskName(taskType)}\n\n` +
        'Выбери следующую задачу или заверши отчёт:',
        reportKeyboard,
      );
    }
  });

  // Обработка неизвестных команд
  bot.on('message', async (ctx) => {
    if ('text' in ctx.message && ctx.message.text.startsWith('/')) {
      await ctx.reply(
        'Неизвестная команда 🤔\n' +
        'Используй /help для списка доступных команд.',
        mainKeyboard,
      );
    }
  });

  // Обработка ошибок
  bot.catch((error, ctx) => {
    logger.error({ userId: ctx.from?.id, error },
      `Bot error for user ${ctx.from?.id}`);

    ctx.reply('😔 Произошла ошибка. Пожалуйста, попробуйте позже.')
      .catch((err) => logger.error('Failed to send error message:', err));
  });
};

// Вспомогательная функция
const getTaskName = (type: string): string => {
  const names: Record<string, string> = {
    leetcode: 'задач LeetCode',
    applications: 'откликов',
  };
  return names[type] || type;
};