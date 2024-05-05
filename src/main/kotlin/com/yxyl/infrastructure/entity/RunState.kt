package com.yxyl.infrastructure.entity

/** A structure used to cache the current state of Attention activation */
data class RunState(val config: ModelWeight.Config) {
    // activation at current time stamp (dim,)
    val x = FloatArray(config.dim)

    //  activation inside a residual branch
    val xb = FloatArray(config.dim)

    // an additional buffer just for convenience (dim,)
    val xb2 = FloatArray(config.dim)

    // buffer for hidden dimension in the ffn (hidden_dim,)
    val hb = FloatArray(config.hiddenDim)

    // buffer for hidden dimension in the ffn (hidden_dim,)
    val hb2 = FloatArray(config.hiddenDim)

    // query (dim,)
    val q = FloatArray(config.dim)

    // key (dim,)
    val k = FloatArray(config.dim)

    // value (dim,)
    val v = FloatArray(config.dim)

    // buffer for scores/attention values (n_heads, seq_len)
    val att = FloatArray(config.nHeads * config.seqLen)

    // output logits
    val logits = FloatArray(config.vocabSize)

    // kv cache
    val keyCache = FloatArray(config.nLayers * config.seqLen * config.dim)
    val valueCache = FloatArray(config.nLayers * config.seqLen * config.dim)
}
