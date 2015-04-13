package com.example.jarthur.bookexchange;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

// IMPORTANT! We don't want to change the fields in this class because it might mess up
// Serializable. Be careful!

public class Exchange implements Serializable {

    public Integer exchangeId;
    public Integer loanerId;
    public Integer borrowerId;

    public String bookId;
    public String bookTitle;

    public Date createDate;
    public Date startDate;
    public Date endDate;

    public ExchangeStatus status;

    public Exchange() {}

    public String toString() {
        return String.format("%d\n%d\n%d\n%s\n%s\n%s\n%s\n%s\n%s\n",
                exchangeId, loanerId, borrowerId, bookId, bookTitle,
                createDate.toString(), startDate.toString(), endDate.toString(), status);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(this.toString());
    }


    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // TODO populate the fields of 'this' from the data in 'in'...
        // Use regexes to extract data
    }
}
