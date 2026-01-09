import { setupHandlers } from '@bot/handlers';
import { type BotContext } from '@bot/types';
import { logger } from '@core/logger';
import { Telegraf } from 'telegraf';

const main = async () => {
  try {
    logger.info('🚀 Starting Job Quest Tracker bot...');

    // Проверяем токен
    const token = process.env.TELEGRAM_BOT_TOKEN;
    if (!token) {
      logger.fatal('TELEGRAM_BOT_TOKEN is required in .env file');
      process.exit(1);
    }

    // Создаём бота
    const bot = new Telegraf<BotContext>(token);

    // Настраиваем обработчики
    setupHandlers(bot);

    // Запускаем бота
    await bot.launch({
      dropPendingUpdates: true, // Игнорируем сообщения, пока бот был оффлайн
    });

    // Получаем информацию о боте
    const botInfo = await bot.telegram.getMe();

    logger.info({
      botId: botInfo.id,
      botUsername: botInfo.username,
      botName: botInfo.first_name,
    }, 'Bot started successfully');

    // Graceful shutdown
    process.once('SIGINT', () => bot.stop('SIGINT'));
    process.once('SIGTERM', () => bot.stop('SIGTERM'));
  } catch (error) {
    logger.fatal({ error }, 'Failed to start bot:');

    process.exit(1);
  }
};

await main();
