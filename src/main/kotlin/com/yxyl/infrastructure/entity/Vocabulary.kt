package com.yxyl.infrastructure.entity

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class Vocabulary(tokenizerPath: String, transWeight: ModelWeight) {

    private val logger = LoggerFactory.getLogger(Vocabulary::class.java)

    val vocab = Array(transWeight.config.vocabSize) { "" }

    val vocabScores = FloatArray(transWeight.config.vocabSize)

    private val maxTokenLength: Int

    val bytePieces = CharArray(256 * 2)

    val sortedVocab: Array<TokenIndex>

    data class TokenIndex(val str: String, val id: Int)

    init {
        logger.info("Initializing Vocabulary starting...")
        FileChannel.open(Paths.get(tokenizerPath), StandardOpenOption.READ).use { channel ->
            val bb: ByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
            bb.order(ByteOrder.LITTLE_ENDIAN)
            maxTokenLength = bb.getInt()
            for (i in 0 until transWeight.config.vocabSize) {
                vocabScores[i] = bb.getFloat()
                val len = bb.getInt()
                val bytes = ByteArray(len)
                bb[bytes]
                vocab[i] = String(bytes, Charsets.UTF_8)
            }
            for (i in 0 until 256) {
                bytePieces[i * 2] = i.toChar()
                bytePieces[i * 2 + 1] = 0.toChar()
            }
        }

        sortedVocab = Array(transWeight.config.vocabSize) { i ->
            TokenIndex(
                vocab[i], i
            )
        }
        sortedVocab.sortWith(Companion::compareTokens)
        logger.info("Initializing Vocabulary completed.")
    }

    companion object {
        fun compareTokens(a: TokenIndex, b: TokenIndex): Int {
            return a.str.compareTo(b.str)
        }

        fun strLookup(str: String, sortedVocab: Array<TokenIndex>): Int {
            val tok = TokenIndex(str, 0)
            val resIndex = sortedVocab.binarySearch(tok, Companion::compareTokens)
            return if (resIndex >= 0) sortedVocab[resIndex].id else -1
        }
    }
}