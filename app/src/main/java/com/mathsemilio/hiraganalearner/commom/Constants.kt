package com.mathsemilio.hiraganalearner.commom

const val APP_THEME_LIGHT_THEME = 0
const val APP_THEME_DARK_MODE = 1
const val APP_THEME_FOLLOW_SYSTEM = 2

const val GAME_DIFFICULTY_VALUE_BEGINNER = 0
const val GAME_DIFFICULTY_VALUE_MEDIUM = 1
const val GAME_DIFFICULTY_VALUE_HARD = 2

const val SHOW_DIFFICULTY_OPTIONS = "0"
const val DEFAULT_DIFFICULTY_EASY = "1"
const val DEFAULT_DIFFICULTY_MEDIUM = "2"
const val DEFAULT_DIFFICULTY_HARD = "3"

const val ONE_SECOND = 1000L
const val COUNTDOWN_TIME_BEGINNER = 15000L
const val COUNTDOWN_TIME_MEDIUM = 10000L
const val COUNTDOWN_TIME_HARD = 5000L

const val PROGRESS_BAR_MAX_VALUE_BEGINNER = 14
const val PROGRESS_BAR_MAX_VALUE_MEDIUM = 9
const val PROGRESS_BAR_MAX_VALUE_HARD = 4

const val PRIORITY_LOW = 1
const val PRIORITY_MEDIUM = 2
const val PRIORITY_HIGH = 3

const val PERFECT_SCORE = 48

const val NOTIFICATION_ID = 1000
const val PENDING_INTENT_REQUEST_ID = 1001
const val NOTIFICATION_CHANNEL_ID = "trainingNotification"

const val NOTIFICATION_WORK_TAG = "trainingNotification"

const val SWITCH_STATE_KEY = "switchState"
const val TIME_CONFIGURED_KEY = "timeConfigured"
const val PERFECT_SCORES_KEY = "perfectScores"
const val APP_THEME_KEY = "appTheme"

const val TRAINING_NOTIFICATION_PREFERENCE_KEY = "training_notification"
const val DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY = "game_difficulty"
const val CLEAR_PERFECT_SCORES_PREFERENCE_KEY = "clear_perfect_scores"
const val SOUND_EFFECTS_VOLUME_PREFERENCE_KEY = "sound_effects"
const val APP_THEME_PREFERENCE_KEY = "app_theme"
const val APP_BUILD_PREFERENCE_KEY = "app_build"

const val INVALID_DEFAULT_GAME_OPTION_EXCEPTION = "Invalid default game difficulty value"
const val INVALID_APP_THEME_VALUE_EXCEPTION = "Invalid app theme value"
const val INVALID_GAME_DIFFICULTY_VALUE_EXCEPTION = "Invalid game difficulty value"
const val ILLEGAL_PREFERENCE_TYPE = "Illegal preference value type"
const val NULL_ACTIVITY_EXCEPTION = "Activity cannot be null"

const val APP_BUILD_VERSION = "stable-0.1.1"