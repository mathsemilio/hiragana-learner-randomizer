/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.common

const val GAME_DIFFICULTY_VALUE_BEGINNER = 0
const val GAME_DIFFICULTY_VALUE_MEDIUM = 1
const val GAME_DIFFICULTY_VALUE_HARD = 2

const val SHOW_DIFFICULTY_OPTIONS = "0"
const val DEFAULT_DIFFICULTY_BEGINNER = "1"
const val DEFAULT_DIFFICULTY_MEDIUM = "2"
const val DEFAULT_DIFFICULTY_HARD = "3"

const val PERFECT_SCORE = 46

const val LIGHT_THEME_VALUE = 0
const val DARK_THEME_VALUE = 1
const val FOLLOW_SYSTEM_THEME_VALUE = 2

const val ONE_SECOND = 1000L
const val COUNTDOWN_TIME_BEGINNER = 15000L
const val COUNTDOWN_TIME_MEDIUM = 10000L
const val COUNTDOWN_TIME_HARD = 5000L

const val PROGRESS_BAR_MAX_VALUE_BEGINNER = 14
const val PROGRESS_BAR_MAX_VALUE_MEDIUM = 9
const val PROGRESS_BAR_MAX_VALUE_HARD = 4

const val NOTIFICATION_ID = 0
const val PENDING_INTENT_REQUEST_ID = 0
const val NOTIFICATION_CHANNEL_ID = "TRAINING_NOTIFICATION"

const val ARG_DIFFICULTY_VALUE = "ARG_DIFFICULTY_VALUE"
const val ARG_SCORE = "ARG_SCORE"

const val ARG_DIALOG_TITLE = "ARG_DIALOG_TITLE"
const val ARG_DIALOG_MESSAGE = "ARG_DIALOG_MESSAGE"
const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"
const val ARG_IS_CANCELABLE = "ARG_IS_CANCELABLE"

const val TAG_WRONG_ANSWER_DIALOG = "WrongAnswerDialog"
const val TAG_CORRECT_ANSWER_DIALOG = "CorrectAnswerDialog"
const val TAG_TIME_OVER_DIALOG = "TimeOverDialog"
const val TAG_GAME_PAUSED_DIALOG = "GamePausedDialog"
const val TAG_EXIT_GAME_DIALOG = "ExitGameDialog"

const val TRAINING_NOTIFICATION_WORK_TAG = "TRAINING_NOTIFICATION"

const val NOTIFICATION_SWITCH_STATE_KEY = "SWITCH_STATE"
const val NOTIFICATION_TIME_SET_KEY = "TIME_SET"
const val PERFECT_SCORES_KEY = "PERFECT_SCORES"
const val THEME_KEY = "THEME"

const val TRAINING_NOTIFICATION_PREFERENCE_KEY = "TRAINING_NOTIFICATION_PREFERENCE"
const val DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY = "DEFAULT_GAME_DIFFICULTY_PREFERENCE"
const val CLEAR_PERFECT_SCORES_PREFERENCE_KEY = "CLEAR_PERFECT_SCORES_PREFERENCE"
const val SOUND_EFFECTS_VOLUME_PREFERENCE_KEY = "SOUND_EFFECTS_PREFERENCE"
const val THEME_PREFERENCE_KEY = "THEME_PREFERENCE"
const val APP_BUILD_PREFERENCE_KEY = "APP_BUILD_PREFERENCE"

const val ILLEGAL_DEFAULT_DIFFICULTY_VALUE = "Invalid game default difficulty Value"
const val ILLEGAL_GAME_DIFFICULTY_VALUE = "Invalid game difficulty value"
const val ILLEGAL_APP_THEME_VALUE = "Invalid app theme value"

const val NULL_ACTIVITY_EXCEPTION = "Activity cannot be null"

const val INTERSTITIAL_TEST_AD_ID = "ca-app-pub-3940256099942544/1033173712"