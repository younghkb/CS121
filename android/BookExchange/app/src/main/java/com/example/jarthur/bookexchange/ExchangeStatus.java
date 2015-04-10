package com.example.jarthur.bookexchange;

// Note: this is an inner class of Exchange in the server code. May refactor to follow this
// structure later.
public enum ExchangeStatus {
    INITIAL,
    RESPONSE,
    ACCEPTED,
    COMPLETED;
}

/* public enum ExchangeType {
    LOAN,
    BORROW;
} */