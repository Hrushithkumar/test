package org.nitya.software.RealEstate.exception;

public class CustomExceptions {

        public static class UserAlreadyExistsException extends RuntimeException {
            public UserAlreadyExistsException(String message) {
                super(message);
            }
        }

        public static class EmailAlreadyExistsException extends RuntimeException {
            public EmailAlreadyExistsException(String message) {
                super(message);
            }
        }

        public static class PhoneNumberAlreadyExistsException extends RuntimeException {
            public PhoneNumberAlreadyExistsException(String message) {
                super(message);
            }
        }

        public static class LastNameAlreadyExistsException extends RuntimeException {
            public LastNameAlreadyExistsException(String message) {
                super(message);
            }
        }

        public static class UserNotFoundException extends RuntimeException {
            public UserNotFoundException(String message) {
                super(message);
            }
        }
    }
