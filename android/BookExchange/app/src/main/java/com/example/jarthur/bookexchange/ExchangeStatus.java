package com.example.jarthur.bookexchange;

// Note: this is an inner class of Exchange in the server code. May refactor to follow this
// structure later.

// TODO remove this (now inside Book and Exchange)
public enum ExchangeStatus {
    INITIAL,
    RESPONSE,
    ACCEPTED,
    COMPLETED;
}
