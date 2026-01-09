import { env } from '@config/env';
import pino from 'pino';

export const logger = pino({
  level: env.LOG_LEVEL || 'info',
  base: {
    service: 'telegram-bot',
    env: env.NODE_ENV || 'development',
  },
  ...(env.NODE_ENV !== 'prod' && {
    transport: {
      target: 'pino-pretty',
      options: {
        colorize: true,
        translateTime: 'HH:MM:ss Z',
        ignore: 'pid,hostname',
        messageFormat: '{msg}',
      },
    },
  }),
  ...(env.NODE_ENV === 'prod' && {
    timestamp: pino.stdTimeFunctions.isoTime,
    formatters: {
      level: (label: string) => ({ level: label }),
    },
  }),
});
