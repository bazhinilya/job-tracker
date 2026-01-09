// Все команды бота
export const BOT_COMMANDS = {
  START: '/start',
  HELP: '/help',
  PLAN: '/plan',
  REPORT: '/report',
  STATS: '/stats',
  VACANCIES: '/vacancies',
  CANCEL: '/cancel',
} as const;

// Описания команд для меню бота
export const COMMAND_DESCRIPTIONS = [
  { command: 'start', description: 'Начать работу с ботом' },
  { command: 'help', description: 'Показать справку по командам' },
  { command: 'plan', description: 'Настроить план подготовки' },
  { command: 'report', description: 'Отправить ежедневный отчёт' },
  { command: 'stats', description: 'Показать статистику' },
  { command: 'vacancies', description: 'Управление вакансиями' },
  { command: 'cancel', description: 'Отменить текущее действие' },
] as const;

// Состояния пользователя
export const USER_STATES = {
  IDLE: 'idle',
  REPORTING: 'reporting',
  PLAN_SETUP: 'plan_setup',
} as const;
