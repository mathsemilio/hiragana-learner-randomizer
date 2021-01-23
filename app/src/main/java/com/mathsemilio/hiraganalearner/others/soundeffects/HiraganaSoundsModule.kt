package com.mathsemilio.hiraganalearner.others.soundeffects

import android.content.Context
import android.media.AudioAttributes
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.ILLEGAL_HIRAGANA_SOUND_KEY
import com.mathsemilio.hiraganalearner.common.playSFX

class HiraganaSoundsModule(private val mContext: Context, private val mVolume: Float) :
    BaseSoundEffectModule() {

    private var mHiraganaSoundA = 0
    private var mHiraganaSoundI = 0
    private var mHiraganaSoundU = 0
    private var mHiraganaSoundE = 0
    private var mHiraganaSoundO = 0

    private var mHiraganaSoundKa = 0
    private var mHiraganaSoundKi = 0
    private var mHiraganaSoundKu = 0
    private var mHiraganaSoundKe = 0
    private var mHiraganaSoundKo = 0

    private var mHiraganaSoundSa = 0
    private var mHiraganaSoundShi = 0
    private var mHiraganaSoundSu = 0
    private var mHiraganaSoundSe = 0
    private var mHiraganaSoundSo = 0

    private var mHiraganaSoundTa = 0
    private var mHiraganaSoundChi = 0
    private var mHiraganaSoundTsu = 0
    private var mHiraganaSoundTe = 0
    private var mHiraganaSoundTo = 0

    private var mHiraganaSoundNa = 0
    private var mHiraganaSoundNi = 0
    private var mHiraganaSoundNu = 0
    private var mHiraganaSoundNe = 0
    private var mHiraganaSoundNo = 0

    private var mHiraganaSoundHa = 0
    private var mHiraganaSoundHi = 0
    private var mHiraganaSoundFu = 0
    private var mHiraganaSoundHe = 0
    private var mHiraganaSoundHo = 0

    private var mHiraganaSoundMa = 0
    private var mHiraganaSoundMi = 0
    private var mHiraganaSoundMu = 0
    private var mHiraganaSoundMe = 0
    private var mHiraganaSoundMo = 0

    private var mHiraganaSoundYa = 0
    private var mHiraganaSoundYu = 0
    private var mHiraganaSoundYo = 0

    private var mHiraganaSoundRa = 0
    private var mHiraganaSoundRi = 0
    private var mHiraganaSoundRu = 0
    private var mHiraganaSoundRe = 0
    private var mHiraganaSoundRo = 0

    private var mHiraganaSoundWa = 0
    private var mHiraganaSoundWo = 0

    private var mHiraganaSoundN = 0

    private val mHiraganaSoundsMap = mutableMapOf<String, Int>()

    init {
        setAudioAttributesContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        loadSoundEffects()
    }

    override fun loadSoundEffects() {
        loadFirstColumnSoundEffects()
        loadSecondColumnSoundEffects()
        loadThirdColumnSoundEffects()
        loadFourthColumnSoundEffects()
        loadFifthColumnSoundEffects()
        loadSixthColumnSoundEffects()
        loadSeventhColumnSoundEffects()
        loadEighthColumnSoundEffects()
        loadNinthColumnSoundEffects()
        loadTenthColumnSoundEffects()
        loadEleventhColumnSoundEffects()
    }

    private fun loadFirstColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundA = load(mContext, R.raw.hir_a, 1)
            mHiraganaSoundI = load(mContext, R.raw.hir_i, 1)
            mHiraganaSoundU = load(mContext, R.raw.hir_u, 1)
            mHiraganaSoundE = load(mContext, R.raw.hir_e, 1)
            mHiraganaSoundO = load(mContext, R.raw.hir_o, 1)
        }

        mHiraganaSoundsMap.apply {
            put("A", mHiraganaSoundA)
            put("I", mHiraganaSoundI)
            put("U", mHiraganaSoundU)
            put("E", mHiraganaSoundE)
            put("O", mHiraganaSoundO)
        }
    }

    private fun loadSecondColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundKa = load(mContext, R.raw.hir_ka, 1)
            mHiraganaSoundKi = load(mContext, R.raw.hir_ki, 1)
            mHiraganaSoundKu = load(mContext, R.raw.hir_ku, 1)
            mHiraganaSoundKe = load(mContext, R.raw.hir_ke, 1)
            mHiraganaSoundKo = load(mContext, R.raw.hir_ko, 1)
        }

        mHiraganaSoundsMap.apply {
            put("KA", mHiraganaSoundKa)
            put("KI", mHiraganaSoundKi)
            put("KU", mHiraganaSoundKu)
            put("KE", mHiraganaSoundKe)
            put("KO", mHiraganaSoundKo)
        }
    }

    private fun loadThirdColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundSa = load(mContext, R.raw.hir_sa, 1)
            mHiraganaSoundShi = load(mContext, R.raw.hir_shi, 1)
            mHiraganaSoundSu = load(mContext, R.raw.hir_su, 1)
            mHiraganaSoundSe = load(mContext, R.raw.hir_se, 1)
            mHiraganaSoundSo = load(mContext, R.raw.hir_so, 1)
        }

        mHiraganaSoundsMap.apply {
            put("SA", mHiraganaSoundSa)
            put("SHI", mHiraganaSoundShi)
            put("SU", mHiraganaSoundSu)
            put("SE", mHiraganaSoundSe)
            put("SO", mHiraganaSoundSo)
        }
    }

    private fun loadFourthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundTa = load(mContext, R.raw.hir_ta, 1)
            mHiraganaSoundChi = load(mContext, R.raw.hir_chi, 1)
            mHiraganaSoundTsu = load(mContext, R.raw.hir_tsu, 1)
            mHiraganaSoundTe = load(mContext, R.raw.hir_te, 1)
            mHiraganaSoundTo = load(mContext, R.raw.hir_to, 1)
        }

        mHiraganaSoundsMap.apply {
            put("TA", mHiraganaSoundTa)
            put("CHI", mHiraganaSoundChi)
            put("TSU", mHiraganaSoundTsu)
            put("TE", mHiraganaSoundTe)
            put("TO", mHiraganaSoundTo)
        }
    }

    private fun loadFifthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundNa = load(mContext, R.raw.hir_na, 1)
            mHiraganaSoundNi = load(mContext, R.raw.hir_ni, 1)
            mHiraganaSoundNu = load(mContext, R.raw.hir_nu, 1)
            mHiraganaSoundNe = load(mContext, R.raw.hir_ne, 1)
            mHiraganaSoundNo = load(mContext, R.raw.hir_no, 1)
        }

        mHiraganaSoundsMap.apply {
            put("NA", mHiraganaSoundNa)
            put("NI", mHiraganaSoundNi)
            put("NU", mHiraganaSoundNu)
            put("NE", mHiraganaSoundNe)
            put("NO", mHiraganaSoundNo)
        }
    }

    private fun loadSixthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundHa = load(mContext, R.raw.hir_ha, 1)
            mHiraganaSoundHi = load(mContext, R.raw.hir_hi, 1)
            mHiraganaSoundFu = load(mContext, R.raw.hir_fu, 1)
            mHiraganaSoundHe = load(mContext, R.raw.hir_he, 1)
            mHiraganaSoundHo = load(mContext, R.raw.hir_ho, 1)
        }

        mHiraganaSoundsMap.apply {
            put("HA", mHiraganaSoundHa)
            put("HI", mHiraganaSoundHi)
            put("FU", mHiraganaSoundFu)
            put("HE", mHiraganaSoundHe)
            put("HO", mHiraganaSoundHo)
        }
    }

    private fun loadSeventhColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundMa = load(mContext, R.raw.hir_ma, 1)
            mHiraganaSoundMi = load(mContext, R.raw.hir_mi, 1)
            mHiraganaSoundMu = load(mContext, R.raw.hir_mu, 1)
            mHiraganaSoundMe = load(mContext, R.raw.hir_me, 1)
            mHiraganaSoundMo = load(mContext, R.raw.hir_mo, 1)
        }

        mHiraganaSoundsMap.apply {
            put("MA", mHiraganaSoundMa)
            put("MI", mHiraganaSoundMi)
            put("MU", mHiraganaSoundMu)
            put("ME", mHiraganaSoundMe)
            put("MO", mHiraganaSoundMo)
        }
    }

    private fun loadEighthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundYa = load(mContext, R.raw.hir_ya, 1)
            mHiraganaSoundYu = load(mContext, R.raw.hir_yu, 1)
            mHiraganaSoundYo = load(mContext, R.raw.hir_yo, 1)
        }

        mHiraganaSoundsMap.apply {
            put("YA", mHiraganaSoundYa)
            put("YU", mHiraganaSoundYu)
            put("YO", mHiraganaSoundYo)
        }
    }

    private fun loadNinthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundRa = load(mContext, R.raw.hir_ra, 1)
            mHiraganaSoundRi = load(mContext, R.raw.hir_ri, 1)
            mHiraganaSoundRu = load(mContext, R.raw.hir_ru, 1)
            mHiraganaSoundRe = load(mContext, R.raw.hir_re, 1)
            mHiraganaSoundRo = load(mContext, R.raw.hir_ro, 1)
        }

        mHiraganaSoundsMap.apply {
            put("RA", mHiraganaSoundRa)
            put("RI", mHiraganaSoundRi)
            put("RU", mHiraganaSoundRu)
            put("RE", mHiraganaSoundRe)
            put("RO", mHiraganaSoundRo)
        }
    }

    private fun loadTenthColumnSoundEffects() {
        getSoundPool().apply {
            mHiraganaSoundWa = load(mContext, R.raw.hir_wa, 1)
            mHiraganaSoundWo = load(mContext, R.raw.hir_wo, 1)
        }

        mHiraganaSoundsMap.apply {
            put("WA", mHiraganaSoundWa)
            put("WO", mHiraganaSoundWo)
        }
    }

    private fun loadEleventhColumnSoundEffects() {
        mHiraganaSoundN = getSoundPool().load(mContext, R.raw.hir_n, 1)
        mHiraganaSoundsMap["N"] = mHiraganaSoundN
    }

    fun playHiraganaSymbolSoundEffect(romanization: String) {
        mHiraganaSoundsMap[romanization]?.let { soundEffectKey ->
            getSoundPool().playSFX(
                soundEffectKey,
                mVolume,
                1)
        } ?: throw IllegalArgumentException(ILLEGAL_HIRAGANA_SOUND_KEY)
    }
}