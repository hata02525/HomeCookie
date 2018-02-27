package com.app.homecookie.Beans;

import java.io.Serializable;

/**
 * Created by fluper on 23/6/17.
 */
public class CardBean implements Serializable {
    private String cardId;
    private String cardNumber;
    private String cardName;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
