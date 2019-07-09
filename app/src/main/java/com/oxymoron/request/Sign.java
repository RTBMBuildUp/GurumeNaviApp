package com.oxymoron.request;

public enum Sign {
    Equal {
        @Override
        public String toString() {
            return "=";
        }
    },
    Question {
        @Override
        public String toString() {
            return "?";
        }
    },
    And {
        @Override
        public String toString() {
            return "&";
        }
    }
}
