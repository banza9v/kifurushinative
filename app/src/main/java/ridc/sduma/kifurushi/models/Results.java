package ridc.sduma.kifurushi.models;

public class Results {
    private long received;
    private long sent;
    private long total;

    public Results(long received, long sent, long total) {
        this.received = received;
        this.sent = sent;
        this.total = total;
    }

    public long getReceived() {
        return received;
    }

    public void setReceived(long received) {
        this.received = received;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
