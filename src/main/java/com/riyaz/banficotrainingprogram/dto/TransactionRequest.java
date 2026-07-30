package com.riyaz.banficotrainingprogram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

    public class TransactionRequest {

        private String type;
        private Integer amount;

        public TransactionRequest() {
        }

        @NotBlank(message = "Transaction type is required")
        public String getType() {
            return type;
        }

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        public Integer getAmount() {
            return amount;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }
