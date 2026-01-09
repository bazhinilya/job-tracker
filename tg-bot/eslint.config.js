import js from '@eslint/js';
import typescriptPlugin from '@typescript-eslint/eslint-plugin';
import typescriptParser from '@typescript-eslint/parser';

export default [
  // Базовый конфиг
  js.configs.recommended,

  // TypeScript конфиг с type information
  {
    files: ['**/*.ts'],
    languageOptions: {
      parser: typescriptParser,
      parserOptions: {
        project: './tsconfig.json',
        tsconfigRootDir: process.cwd(),
      },
      ecmaVersion: 'latest',
      sourceType: 'module',
      globals: {
        Bun: 'readonly',
        console: 'readonly',
        process: 'readonly',
        setTimeout: 'readonly',
        setInterval: 'readonly',
        clearTimeout: 'readonly',
        clearInterval: 'readonly',
      },
    },
    plugins: {
      '@typescript-eslint': typescriptPlugin,
    },
    rules: {
      'indent': ['error', 2, {
        'SwitchCase': 1 // case на том же уровне, что и switch
      }], // 2 пробела вместо табов
      'semi': ['error', 'always'], // всегда точки с запятой
      'quotes': ['error', 'single'], // одинарные кавычки
      'comma-dangle': ['error', 'always-multiline'], // висящие запятые в multiline
      'object-curly-spacing': ['error', 'always'], // пробелы в фигурных скобках
      'array-bracket-spacing': ['error', 'never'], // без пробелов в квадратных скобках
      'comma-spacing': ['error', { 'before': false, 'after': true }], // пробелы после запятых
      'arrow-spacing': ['error', { 'before': true, 'after': true }], // пробелы вокруг стрелок
      'brace-style': ['error', '1tbs'], // стиль скобок
      'keyword-spacing': ['error', { 'before': true, 'after': true }], // пробелы вокруг ключевых слов

      // Удаляет лишние пробелы в конце строк
      'no-trailing-spaces': 'error',
      // Запрещает смешивание пробелов и табов
      // Требует пробел после // в комментариях
      'spaced-comment': ['error', 'always'],
      'no-mixed-spaces-and-tabs': 'error',
       // Запрещает пустые блоки кода
      'no-empty': 'error',
      // Запрещает ненужные пустые строки в начале/конце блоков
      'padded-blocks': ['error', 'never'],
      // Требует пробелы внутри фигурных скобок
      'object-curly-spacing': ['error', 'always'],
      'no-multiple-empty-lines': ['error', {
        'max': 1,
        'maxEOF': 1,
        'maxBOF': 0
      }],

      '@typescript-eslint/no-unused-vars': 'error',
      '@typescript-eslint/no-explicit-any': 'warn',
      '@typescript-eslint/no-floating-promises': 'error',
      '@typescript-eslint/no-misused-promises': 'error',
      // Качество TypeScript кода
      '@typescript-eslint/no-explicit-any': 'error',
      '@typescript-eslint/no-inferrable-types': 'error',
      '@typescript-eslint/array-type': ['error', { 'default': 'array-simple' }],
      '@typescript-eslint/consistent-type-definitions': ['error', 'interface'],
      '@typescript-eslint/consistent-type-imports': ['error', {
        'prefer': 'type-imports',
        'fixStyle': 'inline-type-imports'
      }],
      '@typescript-eslint/no-unnecessary-type-assertion': 'error',
      '@typescript-eslint/prefer-for-of': 'error',
      '@typescript-eslint/prefer-function-type': 'error',

      // Асинхронный код
      '@typescript-eslint/no-floating-promises': 'error',
      '@typescript-eslint/await-thenable': 'error',
      '@typescript-eslint/no-misused-promises': 'error',

      // Стиль кода
      'prefer-arrow-callback': 'error',
      'arrow-body-style': ['error', 'as-needed'],
      'no-var': 'error',
      'prefer-const': 'error',
      'eqeqeq': ['error', 'always'],
      'no-console': 'warn',
      'no-alert': 'error',
      'no-debugger': 'error',
      'curly': ['error', 'all'],
      'dot-notation': 'error',

    },
  },

  // Игнорируемые файлы
  {
    ignores: [
      'node_modules/',
      'dist/',
      'coverage/',
      '**/*.d.ts',
      '*.config.js'
    ],
  },
];