package com.yxyl.domain.inference.service.token

import com.yxyl.infrastructure.entity.Vocabulary

interface Tokenizer {

    /**
     * Encodes a [text] sequence into an array of integer [tokens] using the provided [vocabulary].
     *
     * This function takes a [text] input and encodes it into a sequence of integer [tokens]
     * based on the given [vocabulary].
     * It also supports the addition of optional beginning-of-sequence [bos] and end-of-sequence [eos] tokens.
     *
     * @since 0.1.0
     */
    fun encode(vocabulary: Vocabulary, text: String, bos: Byte, eos: Byte, tokens: IntArray): Int

    /**
     * Decodes an integer token into a text piece using the provided [vocabulary].
     *
     * @since 0.1.0
     */
    fun decode(vocabulary: Vocabulary, token: Int): String
}

class UTF8Tokenizer : Tokenizer {

    override fun encode(
        vocabulary: Vocabulary,
        text: String,
        bos: Byte,
        eos: Byte,
        tokens: IntArray
    ): Int {

        // create a temporary buffer that will store merge candidates of always two consecutive tokens
        // *2 for concat, +1 for null terminator +2 for UTF8 (in case maxTokenLength is 1)
        var strLen = 0

        // start at 0 tokens
        var nTokens = 0

        // add optional BOS (=1) token, if desired
        if (bos.toInt() == 1) {
            tokens[nTokens++] = 1
        }

        // add dummy prefix if text is not empty
        if (text.isNotEmpty()) {
            val dummyPrefix = Vocabulary.strLookup(" ", vocabulary.sortedVocab)
            tokens[nTokens++] = dummyPrefix
        }

        // UTF-8 processing
        for (c in text) {
            if ((c.code and 0xC0) != 0x80) {
                strLen = 0
            }
            strLen++


            val id = Vocabulary.strLookup(c.toString(), vocabulary.sortedVocab)

            if (id != -1) {
                tokens[nTokens++] = id
            } else {
                for (i in 0 until strLen) {
                    tokens[nTokens++] = c.code + 3
                }
            }
            strLen = 0
        }

        // merge consecutive tokens
        while (true) {
            var bestScore = -1e10
            var bestId = -1
            var bestIdx = -1

            for (i in 0 until nTokens - 1) {
                val str = "${vocabulary.vocab[tokens[i]]}${vocabulary.vocab[tokens[i + 1]]}"
                val id = Vocabulary.strLookup(str, vocabulary.sortedVocab)
                if (id != -1 && vocabulary.vocabScores[id] > bestScore) {
                    bestScore = vocabulary.vocabScores[id].toDouble()
                    bestId = id
                    bestIdx = i
                }
            }

            if (bestIdx == -1) {
                break
            }

            tokens[bestIdx] = bestId
            for (i in bestIdx + 1 until nTokens - 1) {
                tokens[i] = tokens[i + 1]
            }
            nTokens--
        }

        // add optional EOS (=2) token, if desired
        if (eos.toInt() != 0) {
            tokens[nTokens++] = 2
        }
        return nTokens
    }

    override fun decode(vocabulary: Vocabulary, token: Int): String {
        val inputString = vocabulary.vocab[token]
        return "<0x([0-9A-Fa-f]{2})>".toRegex().replace(inputString) { result ->
            val hexValue = result.groupValues[1]
            val byteVal = hexValue.toInt(16)

            if (byteVal < vocabulary.bytePieces.size) {
                vocabulary.bytePieces[byteVal * 2].toString()
            } else {
                inputString
            }
        }
    }

}