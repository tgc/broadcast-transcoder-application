package dk.statsbiblioteket.broadcasttranscoder.persistence.entities;

import dk.statsbiblioteket.broadcasttranscoder.persistence.TranscodingStateEnum;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: abr
 * Date: 1/28/13
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TranscodingRecord extends Identifiable<String>{

    private Long lastTranscodedTimestamp;
    private Long domsLatestTimestamp;
    private String failureMessage;


    @Enumerated(EnumType.STRING)
    private TranscodingStateEnum transcodingState;


    public Long getLastTranscodedTimestamp() {
        return lastTranscodedTimestamp;
    }

    public void setLastTranscodedTimestamp(Long lastTranscodedTimestamp) {
        this.lastTranscodedTimestamp = lastTranscodedTimestamp;
    }

    public Long getDomsLatestTimestamp() {
        return domsLatestTimestamp;
    }

    public void setDomsLatestTimestamp(Long domsLatestTimestamp) {
        this.domsLatestTimestamp = domsLatestTimestamp;
    }

    public TranscodingStateEnum getTranscodingState() {
        return transcodingState;
    }

    public void setTranscodingState(TranscodingStateEnum transcodingState) {
        this.transcodingState = transcodingState;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TranscodingRecord)) return false;

        TranscodingRecord that = (TranscodingRecord) o;

        if (domsLatestTimestamp != null ? !domsLatestTimestamp.equals(that.domsLatestTimestamp) : that.domsLatestTimestamp != null)
            return false;
        if (failureMessage != null ? !failureMessage.equals(that.failureMessage) : that.failureMessage != null)
            return false;
        if (lastTranscodedTimestamp != null ? !lastTranscodedTimestamp.equals(that.lastTranscodedTimestamp) : that.lastTranscodedTimestamp != null)
            return false;
        if (transcodingState != that.transcodingState) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lastTranscodedTimestamp != null ? lastTranscodedTimestamp.hashCode() : 0;
        result = 31 * result + (domsLatestTimestamp != null ? domsLatestTimestamp.hashCode() : 0);
        result = 31 * result + (failureMessage != null ? failureMessage.hashCode() : 0);
        result = 31 * result + (transcodingState != null ? transcodingState.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TranscodingRecord{" +
                "lastTranscodedTimestamp=" + lastTranscodedTimestamp +
                ", domsLatestTimestamp=" + domsLatestTimestamp +
                ", failureMessage='" + failureMessage + '\'' +
                ", transcodingState=" + transcodingState +
                '}';
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

}

