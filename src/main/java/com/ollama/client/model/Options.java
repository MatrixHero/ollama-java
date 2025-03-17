package com.ollama.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration options for the Ollama client.
 * Contains various parameters for model loading and runtime settings.
 */
@Data
@NoArgsConstructor
public class Options {
    // Load time options
    /** Whether to enable NUMA */
    private Boolean numa;
    /** Context window size */
    private Integer numCtx;
    /** Batch size */
    private Integer numBatch;
    /** Number of GPUs */
    private Integer numGpu;
    /** Main GPU index */
    private Integer mainGpu;
    /** Whether to use low VRAM mode */
    private Boolean lowVram;
    /** Whether to use FP16 KV cache */
    private Boolean f16Kv;
    /** Whether to output all logits */
    private Boolean logitsAll;
    /** Whether to load vocabulary only */
    private Boolean vocabOnly;
    /** Whether to use memory mapping */
    private Boolean useMmap;
    /** Whether to lock memory */
    private Boolean useMlock;
    /** Whether to use for embedding only */
    private Boolean embeddingOnly;
    /** Number of threads */
    private Integer numThread;

    // Runtime options
    /** Number of tokens to keep */
    private Integer numKeep;
    /** Random seed */
    private Integer seed;
    /** Number of tokens to predict */
    private Integer numPredict;
    /** Number of top-k tokens to keep during sampling */
    private Integer topK;
    /** Top-p value for sampling */
    private Double topP;
    /** Z value for Tail Free Sampling */
    private Double tfsZ;
    /** P value for Typical Sampling */
    private Double typicalP;
    /** Number of tokens for repeat penalty */
    private Integer repeatLastN;
    /** Temperature parameter */
    private Double temperature;
    /** Repeat penalty coefficient */
    private Double repeatPenalty;
    /** Presence penalty coefficient */
    private Double presencePenalty;
    /** Frequency penalty coefficient */
    private Double frequencyPenalty;
    /** Whether to enable Mirostat sampling */
    private Integer mirostat;
    /** Target entropy for Mirostat */
    private Double mirostatTau;
    /** Learning rate for Mirostat */
    private Double mirostatEta;
    /** Whether to penalize newlines */
    private Boolean penalizeNewline;
    /** List of tokens to stop generation */
    private String[] stop;
} 