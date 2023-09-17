package com.casleuth.casleuthbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cas_model {
    String fastaId;
    String guideSeq;
    int index;
    String modelType;
    double score;

    public String getFastaId() {
        return fastaId;
    }

    public void setFastaId(String fastaId) {
        this.fastaId = fastaId;
    }

    public String getGuideSeq() {
        return guideSeq;
    }

    public void setGuideSeq(String guideSeq) {
        this.guideSeq = guideSeq;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
