package com.casleuth.casleuthbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class virus {
    int virusId;
    String accession;
    String organismName;
    String isolate;
    String species;
    String family;
    int length;
    String segment;
    String geoLocation;
    String host;
    String sequence;
    String virusType;

    public int getVirusId() {
        return virusId;
    }

    public void setVirusId(int virusId) {
        this.virusId = virusId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getOrganismName() {
        return organismName;
    }

    public void setOrganismName(String organismName) {
        this.organismName = organismName;
    }

    public String getIsolate() {
        return isolate;
    }

    public void setIsolate(String isolate) {
        this.isolate = isolate;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getVirusType() {
        return virusType;
    }

    public void setVirusType(String virusType) {
        this.virusType = virusType;
    }
}
